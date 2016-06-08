package com.mac.rso

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.mac.rso.CommitActor.Messages._
import com.typesafe.scalalogging.Logger
import org.json4s.native.Serialization.write
import org.slf4j.LoggerFactory
import shapeless.syntax.typeable._
import org.json4s.DefaultFormats

import scala.util.parsing.json.{JSON, JSONArray, JSONObject}

/**
  * Created by mac on 17.04.16.
  */
object CommitActor {

  object Messages {

    trait Message

    case object VoteOk extends Message

    case object VoteError extends Message

    case object Vote extends Message

    case object Commit extends Message

    case object Rollback extends Message

    case object Ack extends Message

    case object UnknownError extends Message

  }

}

class CommitActor(dbHostUrl: String, documents: List[Map[String, Any]], txId: String) extends Actor {

  import Tcp._
  import context.system


  implicit val jsonFormats = DefaultFormats

  val log = Logger(LoggerFactory.getLogger(classOf[CommitActor].getName))

  val hostname = dbHostUrl.split(":")(0)
  val port = dbHostUrl.split(":")(1).toInt

  var listener: Option[ActorRef] = None

  override def receive: Receive = {
    case Vote =>
      listener = Some(sender())
      val remote: InetSocketAddress = new InetSocketAddress(hostname, port)
      IO(Tcp) ! Connect(remote)

    case CommandFailed(_: Connect) =>
      log.info("connection failed")
      listener.get ! VoteError
      context stop self

    case c@Connected(remote, local) =>
      val connection = sender()
      connection ! Register(self)

      val command: JSONObject = JSONObject(Map(
        "command" -> Messages2PC.VOTE,
        "txId" -> txId,
        "objects" -> JSONArray(documents map { doc => write(doc) })))

      connection ! Write(ByteString(command.toString(), "UTF-8"))

      context become {
        case Received(data) =>
          JSON.parseFull(data.utf8String).get.cast[Map[String, Any]] match {
            case Some(response) =>
              response.get("command") match {
                case Some(Messages2PC.VOTE_OK) =>
                  log.info("received VoteOk!!")
                  listener.get ! VoteOk
                case Some(Messages2PC.ACK) =>
                  log.info("received ack!!")
                  listener.get ! Ack
                case a: Any =>
                  log.error("unknown response:" + a)
                  listener.get ! UnknownError
              }
            case a: Any =>
              log.error("unknown response: " + a)
              listener.get ! UnknownError
          }

        case Commit =>
          listener = Some(sender())
          val command: JSONObject = JSONObject(Map("command" -> Messages2PC.COMMIT))
          connection ! Write(ByteString(command.toString(), "UTF-8"))

        case Rollback =>
          listener = Some(sender())
          val command: JSONObject = JSONObject(Map("command" -> Messages2PC.ROLLBACK))
          connection ! Write(ByteString(command.toString(), "UTF-8"))
          listener.get ! Ack
      }
  }
}
