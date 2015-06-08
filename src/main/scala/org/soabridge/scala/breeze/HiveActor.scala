package org.soabridge.scala.breeze

import akka.actor.{Props, Actor}

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] class HiveActor extends Actor {
  /* Importing all messages declared in companion object for processing */
  import HiveActor.Messages._

  /** Message processing */
  def receive: Receive = {
    case Status =>
  }
}

/**
 * Missing documentation.
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] object HiveActor {
  /** Actor properties for HiveActor */
  val props: Props = Props[HiveActor]

  /** Accepted messages for HiveActor */
  object Messages {
    case class Status
  }
}
