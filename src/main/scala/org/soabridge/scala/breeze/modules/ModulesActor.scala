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
  private var modules: Seq[ActorRef] = startModuleHandler(Seq(), settings.modules: _*)

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
    case Terminated(moduleHandler) =>
      modules = modules filterNot (_ equals moduleHandler)
    case AddModule(moduleHandlerSettings) =>
      modules = startModuleHandler(modules, moduleHandlerSettings)
    case RemoveModule(moduleHandlerSettings) =>
      shutdownModuleRemove(moduleHandlerSettings)
  }


  private def startModuleHandler(list: Seq[ActorRef], settings: ModuleHandlerSettings*): Seq[ActorRef] = settings.toList match {
    case Nil => list
    case head :: tail =>
      val handler = context.actorOf(ModuleHandler.props(settings.head), head.name)
      startModuleHandler(handler +: list, settings.tail:_*)
  }

  private def shutdownModuleRemove(settings: ModuleHandlerSettings): Unit = context child settings.name match {
    case Some(mod) => mod ! ModuleHandler.Requests.Shutdown
  }

  private def handleShutdown(forced: Boolean): Unit = {
    /* Shutdown all configured modules */
    modules foreach { mod =>
      mod ! ModuleHandler.Requests.Shutdown
    }
  }

  private def handleStartup(): Unit = {
    /* Start all configured modules */
    modules foreach { mod =>
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
