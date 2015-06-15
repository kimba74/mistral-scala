package org.soabridge.scala.breeze.modules

import com.typesafe.config.Config

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleSettings(conf: Config, final val name: String) {

  private def prop(pn: String) = name + "." + pn

  final val workerType = conf.getString(prop("worker.type"))
  final val mailboxType = conf.getString(prop("mailbox.type"))
}
