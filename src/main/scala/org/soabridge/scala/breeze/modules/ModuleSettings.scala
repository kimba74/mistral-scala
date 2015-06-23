package org.soabridge.scala.breeze.modules

import akka.actor.Actor
import com.typesafe.config.Config
import scala.reflect.runtime.{universe => ru}

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleSettings(val conf: Config, final val name: String) {

  private def validate(config: Config):Boolean = true // TODO slk: implement validation logic

  final val mailboxParams = conf.getConfig("mailbox.params")

  final val mailboxType = Class.forName(conf.getString("mailbox.type")) // TODO slk: figure out reflection in Scala

  final val workerParams = conf.getString("worker.params")

  final val workerPoolSize = conf.getInt("worker.pool-size")

  final val workerType = Class.forName(conf.getString("worker.type"))   // TODO slk: figure out reflection in Scala
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
