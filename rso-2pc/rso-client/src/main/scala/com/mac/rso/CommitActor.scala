package com.mac.rso

import java.net.InetSocketAddress
import shapeless.syntax.typeable._
import shapeless.syntax.typeable._

import akka.actor.{Actor, ActorRef}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.mac.rso.CommitActor.Messages._
import org.mongodb.scala.bson.collection.immutable.Document

import scala.util.parsing.json.{JSON, JSONObject}

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

class CommitActor(dbHostUrl: String, document: Document, txId: String) extends Actor {

  import Tcp._
  import context.system

  val hostname = dbHostUrl.split(":")(0)
  val port = dbHostUrl.split(":")(1).toInt

  var listener: Option[ActorRef] = None

  override def receive: Receive = {
    case Vote =>
      listener = Some(sender())
      val remote: InetSocketAddress = new InetSocketAddress(hostname, port)
      IO(Tcp) ! Connect(remote)

    case CommandFailed(_: Connect) =>
      println("connection failed")
      listener.get ! VoteError
      context stop self

    case c@Connected(remote, local) =>
      val connection = sender()
      connection ! Register(self)

      val command: JSONObject = JSONObject(Map("command" -> Messages2PC.VOTE, "txId" -> txId, "object" -> document.toJson))

      connection ! Write(ByteString(command.toString(), "UTF-8"))

      context become {
        case Received(data) =>
          JSON.parseFull(data.utf8String).cast[Map[String, Any]] match {
            case Some(response) =>
              response.get("command") match {
                case Some(Messages2PC.VOTE_OK) =>
                  println("received VoteOk!!")
                  listener.get ! VoteOk
                case Some(Messages2PC.ACK) =>
                  println("received ack!!")
                  listener.get ! Ack
                case _ =>
                  println("unknown response")
                  listener.get ! UnknownError
              }
            case _ =>
              println("unknown response")
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
