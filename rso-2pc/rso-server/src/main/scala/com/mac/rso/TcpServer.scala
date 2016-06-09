package com.mac.rso

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}
import org.mongodb.scala._

/**
  * Created by mac on 17.04.16.
  */
class TcpServer(port: Int) extends Actor with ActorLogging {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("0.0.0.0", port))
  log.info("started tcp server on port: " + port)

  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("test")
  val votes: MongoCollection[Document] = database.getCollection("votes")

  override def receive: Receive = {
    case CommandFailed(_: Bind) => context stop self

    case c@Connected(remote, local) =>
      val handler = context.actorOf(Props(new DbAgentActor(votes)))
      val connection = sender()
      connection ! Register(handler)
  }
}
