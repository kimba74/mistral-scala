package org.soabridge.scala.breeze.messaging

/**
 *
 *
 * @author <a href="divyesh.vallabh@soabridge.com">Divyesh Vallabh</a>
 * @since 1.0
 */


object AbstractPayloadType {

  abstract class PayloadType {
    type T
    val value: T
  }

  class PayloadAnyType(val value: Any) extends PayloadType {
    type T = Any
  }

  class PayloadAnyValType(val value: AnyVal) extends PayloadType {
    type T = AnyVal
  }

  class PayloadAnyRefType(val value: AnyRef) extends PayloadType {
    type T = AnyRef
  }
}
