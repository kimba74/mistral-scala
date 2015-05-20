package org.soabridge.scala.breeze.messaging


import scala.collection.immutable.HashMap
/**
 * This trait is the top parent of all Hive Messages exchanged on the bus.
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
trait AllHiveMessages {

  var header : HashMap[String,Any] = _
  val id = java.util.UUID.randomUUID.toString
  var payload : AbstractPayloadType.PayloadType = _
  var session: HashMap[String,Any] = _

}
