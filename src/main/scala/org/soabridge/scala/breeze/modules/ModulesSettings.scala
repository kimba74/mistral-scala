package org.soabridge.scala.breeze.modules

import com.typesafe.config.Config

/**
 * Missing Documentation
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModulesSettings(val conf: Config) {

  /**
   * Returns the configuration object of each configured module.
   */
  val modules: Seq[ModuleHandlerSettings] = Seq[ModuleHandlerSettings]()
}

object ModulesSettings {

  def apply(conf: Config): ModulesSettings = new ModulesSettings(conf)
}
