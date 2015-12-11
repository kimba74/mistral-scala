package org.soabridge.scala.breeze.modules

import akka.actor.SupervisorStrategy.Resume
import akka.actor._
import org.soabridge.scala.breeze.modules.ModulesActor.Requests

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

  /* Create actor the list of configured modules */
  private val modules: Seq[(String, ActorRef)] = settings.modules.map { module =>
    val name = s"module-${module.name}"
    val handler = context.actorOf(ModuleHandler.props(module), name)
    // Register this actor as watchdog for the module actors
    context.watch(handler)
    // Construct the name -> ActorRef tuple
    (name, handler)
  }

  /** Supervisor strategy for the subordinate module handlers. */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    // TODO slk: implement supervisor strategy
    case _ => Resume
  }

  /** Message processing */
  def receive: Receive =  {
    case Start =>
      handleStartup()
    case Status =>
      handleStatusRequest()
    case Shutdown(forced) =>
      handleShutdown(forced)
    case Terminated(module) =>
      handleModuleTermination(module)
  }

  private def handleShutdown(forced: Boolean): Unit = {
    /* Shutdown all configured modules */
    modules foreach { case (name, mod) =>
      mod ! ModuleHandler.Requests.Shutdown
    }
  }

  private def handleStartup(): Unit = {
    /* Start all configured modules */
    modules foreach { case (name, mod) =>
      mod ! ModuleHandler.Requests.Start
    }
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
    /* ModulesActor control messages */
    case object Start
    case object Status
    case class Shutdown(forced: Boolean = false)
  }

  object Responses {
    case class StatusResponse()
  }
}
