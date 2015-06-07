package org.soabridge.scala.breeze.modules

import java.util.concurrent.ConcurrentLinkedQueue

import akka.actor.{ActorSystem, ActorRef}
import akka.dispatch.{ProducesMessageQueue, Envelope, MessageQueue, MailboxType}

/**
 * Missing documentation. 
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class SelectiveMailbox extends MailboxType with ProducesMessageQueue[SelectiveMailbox.SelectiveMessageQueue] {
  import org.soabridge.scala.breeze.modules.SelectiveMailbox._

  def create(owner: Option[ActorRef], system: Option[ActorSystem]): MessageQueue = new SelectiveMessageQueue
}

object SelectiveMailbox {
   class SelectiveMessageQueue extends MessageQueue with SelectiveMessageQueueSemantics {
     /* Backing the selective queue by a java.util.concurrent.ConcurrentLinkedQueue */
     private final val queue = new ConcurrentLinkedQueue[Envelope]()

     def enqueue(receiver: ActorRef, handle: Envelope): Unit = {
       //TODO slk: inject selection logic here
       queue.offer(handle)
     }

     def numberOfMessages: Int = queue.size()

     def dequeue(): Envelope = queue.poll()

     def cleanUp(owner: ActorRef, deadLetters: MessageQueue): Unit = {
       while (hasMessages) {
         deadLetters.enqueue(owner, dequeue())
       }
     }

     def hasMessages: Boolean = !queue.isEmpty
   }
}

trait SelectiveMessageQueueSemantics