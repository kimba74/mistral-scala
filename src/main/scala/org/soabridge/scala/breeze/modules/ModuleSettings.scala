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

  final val mailboxParams: Config = conf.getConfig("mailbox.params")

  // TODO slk: look into Scala reflection Mirrors
  final val mailboxType = Class.forName(conf.getString("mailbox.type")) // TODO slk: figure out reflection in Scala

  final val workerParams: String = conf.getString("worker.params")

  final val workerPoolSize: Int = conf.getInt("worker.pool-size")

  final val workerType: Option[Props] = {
    val actor = Class.forName(conf.getString("worker.type"))
    if (Actor.getClass.isAssignableFrom(actor))
      Some(Props(actor))
    else
      None
  }
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
