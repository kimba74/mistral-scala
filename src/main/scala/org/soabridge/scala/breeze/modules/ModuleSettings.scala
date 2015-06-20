package org.soabridge.scala.breeze.modules

import com.typesafe.config.Config

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleSettings(val conf: Config, final val name: String) {

  final val mailboxParams = conf.getConfig("mailbox.params")

  final val mailboxType = conf.getString("mailbox.type")

  final val workerParams = conf.getString("worker.params")

  final val workerPoolSize = conf.getInt("worker.pool-size")

  final val workerType = conf.getString("worker.type")
}
