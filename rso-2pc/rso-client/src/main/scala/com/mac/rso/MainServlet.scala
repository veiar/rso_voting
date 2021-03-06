package com.mac.rso

import java.util.UUID

import _root_.akka.actor.{ActorSystem, Props}
import _root_.akka.pattern.ask
import _root_.akka.util.Timeout
import org.mongodb.scala._
import org.scalatra.{FutureSupport, _}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.parsing.json.JSONObject

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._

class MainServlet(system: ActorSystem) extends ScalatraServlet with JacksonJsonSupport with FutureSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  protected implicit def executor: ExecutionContext = system.dispatcher

  protected implicit val timeout = new Timeout(5 seconds)

  // To directly connect to the default server localhost on port 27017
  val mongoClient: MongoClient = MongoClient()

  // Before every action runs, set the content type to be in JSON format.
  before() {
  }

  get("/") {
    "Hello!"
  }

  def run2pc(): Future[JSONObject] = {
    val txId = UUID.randomUUID().toString()

    val jsonVotes = parsedBody.values.asInstanceOf[List[Map[String, Any]]]
    val jsonVotesWithIds = jsonVotes map { voteObj => (voteObj
      + ("rowId" -> UUID.randomUUID().toString())
      + ("txId" -> txId))
    }

    val ciActor = system.actorOf(Props(new TwoPhaseCommitActor(txId, jsonVotesWithIds)))
    ciActor ? "go" map {
      case TwoPhaseCommitActor.Ok =>
        "ok"
      case TwoPhaseCommitActor.Error =>
        "error"
      case _ =>
        throw new IllegalStateException("Unknown response from TwoPhaseCommitActor")
    } map {
      s: String => JSONObject(Map("status" -> s))
    }
  }

  post("/") {
    new AsyncResult() {
      override val is: Future[JSONObject] = run2pc()
    }
  }
}
