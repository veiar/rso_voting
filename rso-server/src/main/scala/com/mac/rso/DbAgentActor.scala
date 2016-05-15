package com.mac.rso

import akka.actor.{ActorRef, Actor}
import akka.actor.Status.Success
import akka.io.Tcp
import akka.util.ByteString
import org.bson.conversions.Bson
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala._
import org.mongodb.scala.result.DeleteResult
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.parsing.json.{JSON, JSONObject}

/**
  * Created by mac on 17.04.16.
  */
class DbAgentActor extends Actor {

  import Tcp._

  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("test")
  val votes: MongoCollection[Document] = database.getCollection("votes")

  var jsonToSave: Option[String] = None
  var txId: Option[String] = None

  def receive = {
    case Received(data) =>
      println("received")
      JSON.parseFull(data.decodeString("UTF-8")) match {
        case Some(obj: Map[String, Any]) =>
          obj.get("command") match {
            case Some("vote") =>
              val replyTo = sender()
              println("vote command received, replying voteOk")
              jsonToSave = obj.get("object") match {
                case Some(s: String) => Some(s)
              }

              txId = obj.get("txId") match {
                case Some(s: String) => Some(s)
              }

              respond(replyTo, "vote_ok")
            case Some("commit") =>
              val replyTo = sender()
              println("commit received")
              dbsave() onSuccess {
                case _ =>
                  println("commit successful")
                  respond(replyTo, "ack")
              }

            case Some("rollback") =>
              val replyTo = sender()
              println("rollback received")
              rollback() onSuccess {
                case _ =>
                  println("rollback successful")
                  respond(replyTo, "ack")
              }
          }

        case _ =>
          println("unknown message")
          println(data.toString())
      }
    case PeerClosed =>
      context stop self
    case _ =>
      println("unknown message")
  }

  def respond(replyTo: ActorRef, response: String): Unit = {
    replyTo ! Write(ByteString(JSONObject(Map("command" -> response)).toString(), "UTF-8"))
  }

  def dbsave(): Future[Seq[Completed]] = {
    val doc: Document = Document(jsonToSave.get)

    votes.insertOne(doc).toFuture()
  }

  def rollback(): Future[Seq[DeleteResult]] = {
    votes.deleteMany(Document("txId" -> txId)).toFuture()
  }
}
