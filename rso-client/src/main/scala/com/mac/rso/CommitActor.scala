package com.mac.rso

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.mac.rso.CommitActor._
import org.json4s.jackson.Json
import org.mongodb.scala.bson.collection.immutable.Document

import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import java.net.InetSocketAddress

import scala.util.parsing.json.{JSONType, JSON, JSONObject}

/**
  * Created by mac on 17.04.16.
  */
object CommitActor {

  trait Message

  case object VoteOk extends Message

  case object VoteError extends Message

  case object Vote extends Message

  case object Commit extends Message

  case object Rollback extends Message

  case object Ack extends Message

  case object UnknownError extends Message

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

      val command: JSONObject = JSONObject(Map("command" -> "vote", "txId" -> txId, "object" -> document.toJson))

      connection ! Write(ByteString(command.toString(), "UTF-8"))

      context become {
        case Received(data) =>
          JSON.parseFull(data.decodeString("UTF-8")) match {
            case Some(response: Map[String, Any]) =>
              response.get("command") match {
                case Some("vote_ok") =>
                  println("received VoteOk!!")
                  listener.get ! VoteOk
                case Some("ack") =>
                  println("received ack!!")
                  listener.get ! Ack
                case _ =>
                  println("unknown response")
                  listener.get ! CommitActor.UnknownError
              }
            case _ =>
              println("unknown response")
              listener.get ! CommitActor.UnknownError
          }

        case CommitActor.Commit =>
          listener = Some(sender())
          val command: JSONObject = JSONObject(Map("command" -> "commit"))
          connection ! Write(ByteString(command.toString(), "UTF-8"))

        case CommitActor.Rollback =>
          listener = Some(sender())
          val command: JSONObject = JSONObject(Map("command" -> "rollback"))
          connection ! Write(ByteString(command.toString(), "UTF-8"))
          listener.get ! Ack
      }


  }
}
