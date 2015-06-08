package org.soabridge.scala.breeze.modules

import akka.actor.{Props, Actor}

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] class ModulesActor extends Actor {
  /* Importing all messages declared in companion object for processing */
  import ModulesActor.Messages._

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
private[breeze] object ModulesActor {
  /** Actor properties for ModulesActor */
  val props: Props = Props[ModulesActor]

  /** Accepted messages for ModulesActor */
  object Messages {
    case class Status
  }
}
