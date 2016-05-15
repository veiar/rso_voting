package com.mac.rso

import java.net.InetSocketAddress

import akka.actor.{Actor, Props}
import akka.io.{IO, Tcp}

/**
  * Created by mac on 17.04.16.
  */
class TcpServer(port: Int) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", port))

  override def receive: Receive = {
    case CommandFailed(_: Bind) => context stop self

    case c@Connected(remote, local) =>
      val handler = context.actorOf(Props[DbAgentActor])
      val connection = sender()
      connection ! Register(handler)
  }
}
