package org.soabridge.scala.breeze.resources

import akka.actor.SupervisorStrategy.Resume
import akka.actor._

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] class ResourceActor extends Actor {
  /* Importing all messages declared in companion object for processing */
  import ResourceActor.Messages._

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
private[breeze] object ResourceActor {
  /** Actor properties for ResourceActor */
  val props: Props = Props[ResourceActor]

  /** Accepted messages for ResourceActor */
  object Messages {
    case object Start
    case object Status
    case object Stop
  }
}
