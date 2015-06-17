package org.soabridge.scala.breeze.modules

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import akka.routing.RoundRobinPool
import com.typesafe.config.Config

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleHandler(val settings: ModuleSettings) extends Actor {

  import ModuleHandler.Messages._

  // Creating 10 workers of configured type
  // TODO slk: turn configured worker into object for Props
  // private val workers = context.actorOf(RoundRobinPool(10).props(<actor_here>), settings.name)

  /** */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    // TODO slk: implement supervision strategy for Worker Pool
    case _ => Resume
  }

  /** */
  def receive: Receive = {
    case Start =>
  }
}

/**
 * Missing documentation.
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
object ModuleHandler {
  /** Actor properties for ModuleHandler */
  val props = Props[ModuleHandler]

  /** Accepted messages for ModuleHandler */
  object Messages {
    case object Start
    case object Status
    case object Stop
  }
}