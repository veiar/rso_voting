package com.mac.rso

import akka.actor.{Actor, ActorRef}
import akka.actor.Status.Success
import akka.io.Tcp
import akka.util.ByteString
import com.typesafe.scalalogging.Logger
import org.bson.conversions.Bson
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala._
import org.mongodb.scala.result.DeleteResult
import org.slf4j.LoggerFactory
import shapeless.syntax.typeable._

import scala.concurrent.{Await, Future}
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

  var documentsToSave: Option[List[String]] = None
  var txId: Option[String] = None

  val logger = Logger(LoggerFactory.getLogger(classOf[DbAgentActor].getName))

  def receive = {
    case Received(data) =>
      logger.debug("data received")
      JSON.parseFull(data.utf8String).cast[Map[String, Any]] match {
        case Some(obj) =>
          obj.get("command") match {
            case Some(Messages2PC.VOTE) =>
              val replyTo = sender()
              logger.debug("vote command received, replying voteOk")
              documentsToSave = obj.get("objects") match {
                case Some(s: List[String]) => Some(s)
              }

              txId = obj.get("txId") match {
                case Some(s: String) => Some(s)
                case _ => throw new Exception("txId missing")
              }

              respond(replyTo, Messages2PC.VOTE_OK)
            case Some(Messages2PC.COMMIT) =>
              val replyTo = sender()
              logger.debug("commit received")
              dbsave() onSuccess {
                case _ =>
                  logger.debug("commit successful")
                  respond(replyTo, Messages2PC.ACK)
              }

            case Some(Messages2PC.ROLLBACK) =>
              val replyTo = sender()
              logger.debug("rollback received")
              rollback() onSuccess {
                case _ =>
                  logger.debug("rollback successful")
                  respond(replyTo, Messages2PC.ACK)
              }
            case _ => throw new Exception("unknown command")
          }

        case _ =>
          throw new IllegalStateException("unknown message: " + data.toString())
      }
    case PeerClosed =>
      context stop self
    case _ =>
      logger.debug("unknown message")
  }

  def respond(replyTo: ActorRef, response: String): Unit = {
    replyTo ! Write(ByteString(JSONObject(Map("command" -> response)).toString(), "UTF-8"))
  }

  def dbsave(): Future[Seq[Completed]] = {
    val docs: List[Document] = documentsToSave.get map {
      Document(_)
    }

    votes.insertMany(docs) toFuture()
  }

  def rollback(): Future[Seq[DeleteResult]] = {
    votes.deleteMany(Document("txId" -> txId)).toFuture()
  }
}
