package org.soabridge.scala.breeze.resources

import akka.actor.{Props, Actor}

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] class ResourceActor extends Actor {
  /* Importing all messages declared in companion object for processing */
  import ResourceActor.Messages._

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
private[breeze] object ResourceActor {
  /** Actor properties for ResourceActor */
  val props: Props = Props[ResourceActor]

  /** Accepted messages for ResourceActor */
  object Messages {
    case class Status
  }
}
