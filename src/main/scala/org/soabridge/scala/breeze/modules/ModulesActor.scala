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

  /* Declare and initialize list for all running ModuleHandlers */
  private var modules: Seq[(String, ActorRef)] = addModuleHandler(Seq(), settings.modules: _*)

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
      modules = addModuleHandler(modules, module)
    case RemoveModule(module) =>
      handleModuleRemove(module)
  }


  private def addModuleHandler(list: Seq[(String, ActorRef)], module: ModuleHandlerSettings*): Seq[(String, ActorRef)] = {
    // TODO slk: Look into this logic, not sure it is good!
    if (module.nonEmpty) {
      val handler = context.actorOf(ModuleHandler.props(module.head), s"module-${module.head.name}")
      addModuleHandler((module.head.name, handler) +: list, module.tail:_*)
    }
    else
      list
  }

  private def handleModuleRemove(settings: ModuleHandlerSettings): Unit = {
    // TODO slk: implement shutting down and removing ModuleHandler from list
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
