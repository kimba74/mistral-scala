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
  import ModulesActor.Requests._
  import ModulesActor.Responses._

  /** Supervisor strategy for the subordinate module handlers. */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    //TODO slk: implement supervisor strategy
    case _ => Resume
  }

  /** Message processing */
  def receive: Receive = initialize

  val initialize: Receive = {
    case Start =>
      handleStartup()
    case Status =>
      handleStatusRequest()
  }

  val processing: Receive = {
    case Status =>
      handleStatusRequest()
    case Stop(forced) =>
      handleShutdown(forced)
    case Terminated =>
      //TODO slk: implement watchdog behavior
  }

  private def handleShutdown(forced: Boolean): Unit = {
    // TODO slk: implement stopping procedure
  }

  private def handleStartup(): Unit = {
    //TODO slk: implement module initialization
    context become processing
  }

  private def handleStatusRequest(): Unit = {
    // TODO slk: implement status request-response procedure
    sender ! StatusResponse
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
  object Requests {
    case object Start
    case object Status
    case class Stop(forced: Boolean = false)
  }

  object Responses {
    case class StatusResponse()
  }
}
