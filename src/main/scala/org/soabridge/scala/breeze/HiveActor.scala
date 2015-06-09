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
  val resActor = context.watch(context.actorOf(ResourceActor.props, "resources"))
  /** Supervising actor for all configured module instances */
  val modActor = context.watch(context.actorOf(ModulesActor.props, "modules"))

  /** */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    //TODO slk: implement supervisor strategy
    case Exception => Resume
  }

  /** Message processing */
  def receive: Receive = {
    case Status =>
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
