package org.soabridge.scala.breeze.modules

import akka.actor.SupervisorStrategy.Resume
import akka.actor._

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] class ModulesActor extends Actor {
  /* Importing all messages declared in companion object for processing */
  import ModulesActor.Messages._

  /** Supervisor strategy for the subordinate module handlers. */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    //TODO slk: implement supervisor strategy
    case _ => Resume
  }

  /** Message processing */
  def receive: Receive = initialize

  val initialize: Receive = {
    case Start =>
      //TODO slk: implement module initialization
      context become processing
    case Status =>
      //TODO slk: implement Status behavior
  }

  val processing: Receive = {
    case Status =>
      //TODO slk: implement Status behavior
    case Stop =>
      //TODO slk: implement Stop behavior
    case Terminated =>
      //TODO slk: implement watchdog behavior
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
    case object Start
    case object Status
    case object Stop
  }
}
