package com.mac.rso

import java.util.concurrent.TimeoutException

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.mac.rso.TwoPhaseCommitActor.Result
import com.typesafe.config.ConfigFactory
import org.mongodb.scala.bson.collection.immutable.Document
import akka.pattern.pipe
import com.mac.rso.CommitActor.Messages
import com.mac.rso.CommitActor.Messages._
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by mac on 17.04.16.
  */
object TwoPhaseCommitActor {

  trait Result

  case object Ok extends Result

  case object Error extends Result

}

class TwoPhaseCommitActor(txId: String, documents: List[Map[String, Any]]) extends Actor {

  val conf = ConfigFactory.load()
  val dbHostsConf = sys.env("MONGO_HOSTS").split(",").map(_ + ":8881")
  val minDbsCount = sys.env("REDUNDANCY").toInt

  val logger = Logger(LoggerFactory.getLogger(classOf[TwoPhaseCommitActor].getName))

  protected implicit val timeout = new Timeout(2 seconds)

  override def receive: Receive = {
    case "go" =>
      Future {
        val actorsCommiting: List[ActorRef] = selectActorsForCommit(documents)
        try {
          commitTransaction(actorsCommiting)
          TwoPhaseCommitActor.Ok
        } catch {
          case e: Exception =>
            logger.error("commit error, rolling back", e)
            rollback(actorsCommiting)
            TwoPhaseCommitActor.Error
        }
      } recover {
        case e: Exception =>
          logger.error("unknown error", e)
          TwoPhaseCommitActor.Error
      } pipeTo sender()
  }

  def commitTransaction(actorsCommiting: List[ActorRef]) = {
    val responsesFutures = Future.sequence(actorsCommiting map {
      _ ? Commit recover {
        case e: Exception => Messages.UnknownError
      }
    })
    val commitResponses = Await.result(responsesFutures, Duration.Inf)

    if (!commitResponses.forall(_ == Ack)) {
      throw new Exception("Commit failed")
    }
  }

  def rollback(actorsCommiting: List[ActorRef]) = {
    val responsesFutures = Future.sequence(actorsCommiting map {
      _ ? Rollback recover {
        case e: Exception => Messages.UnknownError
      }
    })

    val commitResponses = Await.result(responsesFutures, Duration.Inf)

    if (!commitResponses.forall(_ == Ack)) {
      throw new Exception("Rollback failed")
    }
  }

  def selectActorsForCommit(documents: List[Map[String, Any]]): List[ActorRef] = {
    var potentialDbsIndicies = dbHostsConf.indices.toList
    val commitingActors: mutable.MutableList[ActorRef] = mutable.MutableList[ActorRef]()

    while (commitingActors.size < minDbsCount) {
      val leftToCommitCount = minDbsCount - commitingActors.size

      // niewystarczająca liczba dostępnych węzłów
      if (leftToCommitCount > potentialDbsIndicies.size) {
        throw new Exception("Not enough db nodes running!")
      }

      // wylosuj hosty
      val dbHostsIndicies = scala.util.Random.shuffle(potentialDbsIndicies).take(leftToCommitCount)

      // usuń wylosowane z puli potencjalnych
      potentialDbsIndicies = potentialDbsIndicies.filterNot(dbHostsIndicies.contains(_))

      val commitActors = dbHostsIndicies.map((dbHostIdx: Int) => {
        dbHostIdx -> context.actorOf(Props(new CommitActor(dbHostsConf(dbHostIdx), documents, txId)))
      })

      val responsesFutures = Future.sequence(
        commitActors map {
          case (hostIdx: Int, actor: ActorRef) =>
            val url = dbHostsConf(hostIdx)
            actor ? Vote recover {
              case e: TimeoutException => Messages.UnknownError
            } map { response => actor -> response }
        })

      val requestResponses = Await.result(responsesFutures, Duration.Inf)

      val actorsVotedOk = requestResponses filter {
        _._2 == VoteOk
      } map {
        _._1
      }

      commitingActors ++= actorsVotedOk
    }

    commitingActors.toList
  }
}
