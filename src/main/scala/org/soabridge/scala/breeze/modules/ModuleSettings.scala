package org.soabridge.scala.breeze.modules

import akka.actor.{Props, Actor}
import com.typesafe.config.Config

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleSettings(val conf: Config, final val name: String) {

  private def validate(config: Config):Boolean = true // TODO slk: implement validation logic

  final val events: Seq[Class[_]] = {
    //conf.get...
    Array(classOf[String])
  }

  final val workerClass: Class[_] = Class.forName(conf.getString("worker.class"))

  final val workerParams: Seq[Any] = {
    //conf.getString("worker.params")
    Array("Test")
  }

  final val poolSize: Int = conf.getInt("worker.pool-size")
}

/**
 * Missing documentation.
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
object ModuleSettings {

  def apply(conf: Config): ModuleSettings = new ModuleSettings(conf, conf.getString("name"))
}
