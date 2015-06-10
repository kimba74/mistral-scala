package org.soabridge.scala.breeze

import akka.actor.SupervisorStrategy.Resume
import akka.actor._
import org.soabridge.scala.breeze.modules.ModulesActor
import org.soabridge.scala.breeze.resources.ResourceActor

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] class HiveActor extends Actor {
  /* Importing all messages declared in companion object for processing */
  import HiveActor.Messages._

  /** Supervising actor for all resources in the hive */
  private var resActor: ActorRef = _
  /** Supervising actor for all configured module instances */
  private var modActor: ActorRef = _

  /** Supervisor strategy for the subordinate actors. */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    //TODO slk: implement supervisor strategy
    case Exception => Resume
  }

  /** Message processing */
  def receive: Receive = initialize

  val initialize: Receive = {
    case Start =>
      // Initializing the subordinate actors
      resActor = context.watch(context.actorOf(ResourceActor.props, "resources"))
      modActor = context.watch(context.actorOf(ModulesActor.props, "modules"))
      // Switching context to 'processing'
      context become processing
    case Status =>
  }

  val processing: Receive = {
    case Status =>
    case Stop =>
    case Terminated =>
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
    case object Start
    case object Status
    case object Stop
  }
}
