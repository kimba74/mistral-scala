package org.soabridge.scala.breeze.modules

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import com.typesafe.config.Config

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleHandler(config: Config) extends Actor {

  import ModuleHandler.Messages._
  // TODO slk: implement Worker Pool

  val settings = new ModuleSettings(config, self.path.name)

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