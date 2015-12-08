package org.soabridge.scala.breeze.modules

import akka.actor.SupervisorStrategy.Resume
import akka.actor._

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
private[breeze] class ModulesActor(settings: ModulesSettings) extends Actor {

  /* Importing all messages declared in companion object for processing */
  import ModulesActor.Requests._
  import ModulesActor.Responses._

  private var modules: Seq[ActorRef] = Seq[ActorRef]()

  /** Supervisor strategy for the subordinate module handlers. */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    //TODO slk: implement supervisor strategy
    case _ => Resume
  }

  /** Message processing */
  def receive: Receive = stateInitialize

  val stateInitialize: Receive = {
    case Start =>
      handleStartup()
    case Status =>
      handleStatusRequest()
  }

  val stateRunning: Receive = {
    case Status =>
      handleStatusRequest()
    case Shutdown(forced) =>
      handleShutdown(forced)
    case Terminated(module) =>
      handleModuleTermination(module)
  }

  private def handleShutdown(forced: Boolean): Unit = {
    // TODO slk: implement stopping procedure
  }

  private def handleStartup(): Unit = {
    // TODO slk: check startup logic
    settings.modules foreach { module =>
      val mod = context.actorOf(ModuleHandler.props(module), s"module-${module.name}")
      mod ! ModuleHandler.Requests.Start
      mod +: modules
    }
    context become stateRunning
  }

  private def handleStatusRequest(): Unit = {
    // TODO slk: implement status request-response procedure
    sender ! StatusResponse
  }

  private def handleModuleTermination(module: ActorRef): Unit = {
    //TODO slk: implement watchdog behavior for terminated module
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
    case class Shutdown(forced: Boolean = false)
  }

  object Responses {
    case class StatusResponse()
  }
}
