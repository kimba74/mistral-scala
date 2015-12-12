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
    case AddModule(module) =>
      handleModuleAdd(module)
    case RemoveModule(module) =>
      handleModuleRemove(module)
  }


  private def handleModuleAdd(settings: ModuleHandlerSettings): Unit = {
    // TODO slk: implement instantiating ModuleHandler and adding it to the list
  }

  private def handleModuleRemove(settings: ModuleHandlerSettings): Unit = {
    // TODO slk: implement terminating ModuleHandler and removing it from the list
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
    /* ModulesActor module control messages */
    case class AddModule(settings: ModuleHandlerSettings)
    case class RemoveModule(settings: ModuleHandlerSettings)
  }

  object Responses {
    case class StatusResponse()
  }
}
