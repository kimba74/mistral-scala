package org.soabridge.scala.breeze.modules

import akka.actor.{Props, Actor}
import com.typesafe.config.Config

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleHandlerSettings(val conf: Config, final val name: String) {

  private def validate(config: Config):Boolean = true // TODO slk: implement validation logic

  /**
   * Returns a sequence of event classes the worker pool will register with
   * on the event stream.
   */
  final val events: Seq[Class[_]] = {
    //conf.get...
    Array(classOf[String])
  }

  /**
   * Returns the class of the worker implementation the pool will be filled with.
   */
  final val workerClass: Class[_] = Class.forName(conf.getString("worker.class"))

  /**
   * Returns a sequence of parameters passed to the worker's constructor.
   */
  final val workerParams: Seq[Any] = {
    //conf.getString("worker.params")
    Array("Test")
  }

  /**
   * Returns the pool size of the worker pool.
   */
  final val poolSize: Int = conf.getInt("worker.pool-size")
}

/**
 * Missing documentation.
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
object ModuleHandlerSettings {

  def apply(conf: Config): ModuleHandlerSettings = new ModuleHandlerSettings(conf, conf.getString("name"))
}
