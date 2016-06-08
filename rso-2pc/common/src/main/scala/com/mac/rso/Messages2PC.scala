package com.mac.rso

/**
  * Created by mac on 15.05.16.
  */
object Messages2PC {

  trait Message2PC extends Serializable

  val VOTE = "vote"

  val VOTE_OK = "vote_ok"

  val ACK = "ack"

  val COMMIT = "commit"

  val ROLLBACK = "rollback"

  val ABORT = "abort"
}
