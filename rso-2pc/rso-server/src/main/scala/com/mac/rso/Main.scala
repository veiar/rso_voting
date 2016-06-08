package com.mac.rso

import akka.actor.{Props, ActorSystem}

/**
  * Created by mac on 17.04.16.
  */
object Main extends App {

  val port = 8881

  val system: ActorSystem = ActorSystem()

  system.actorOf(Props(new TcpServer(port)))
}
