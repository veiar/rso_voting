package com.mac.rso

/**
  * Created by mac on 15.05.16.
  */
object Messages2PC {

  trait Message2PC extends Serializable

  case class Vote(jsonObj: String) extends Message2PC with Serializable

}
