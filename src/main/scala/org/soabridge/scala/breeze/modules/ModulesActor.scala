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
    /* Takes the terminated module out of the modules list */
    case Terminated(moduleHandler) =>
      context.unwatch(moduleHandler)
      modules = modules filterNot (_ equals moduleHandler)
    /* Adds new module handler to modules list and starts it */
    case AddModule(moduleHandlerSettings) =>
      modules = startModuleHandler(modules, moduleHandlerSettings)
    /* Shuts down the module handler for the provided settings */
    case RemoveModule(moduleHandlerSettings) =>
      shutdownModuleHandler(moduleHandlerSettings)
  }


  private def startModuleHandler(list: Seq[ActorRef], settings: ModuleHandlerSettings*): Seq[ActorRef] = settings.toList match {
    case Nil => list
    case head :: tail =>
      /* Create ModuleHandler actor for configured module */
      val handler = context.actorOf(ModuleHandler.props(settings.head), head.name)
      /* Register this actor as watchdog of the ModuleHandler */
      context.watch(handler)
      /* Add ModuleHandler to list and handle the next module configured */
      startModuleHandler(handler +: list, settings.tail:_*)
  }

  private def shutdownModuleHandler(settings: ModuleHandlerSettings): Unit = context child settings.name match {
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
