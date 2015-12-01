package org.soabridge.scala.breeze.modules

import akka.actor.SupervisorStrategy.Resume
import akka.actor._
import akka.routing.RoundRobinPool

/**
 * Missing documentation.
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
class ModuleHandler(settings: ModuleSettings) extends Actor {

  // Import ModuleHandler specific communication objects
  import ModuleHandler.Faults._
  import ModuleHandler.Requests._
  import ModuleHandler.Responses._

  /* NOTE!!
   * The ModuleSettings should include the following parameters
   *   - The unique module name            (String)
   *   - The worker pool size              (Integer)
   *   - The worker actor class            (Class[_] or Class[_ <: Actor])
   *   - The worker actor's parameters     (Seq[Any])
   *   - The event classes to subscribe to (Seq[Class[_]])
   */
  // The ModuleHandler's internal configuration object
  private var handlerSettings = settings

  private var workerPool: Option[ActorRef] = None

  /**
   * Method handling supervisory decisions passed up the chain from
   * the module worker actors.
   * @return The supervisor strategy to use for module workers.
   */
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    // TODO slk: implement supervision strategy for Worker Pool
    case _ => Resume
  }

  /**
   * Sets the initial state of the actor after creation to state "Initialize".
   */
  def receive: Receive = stateInitialize

  /**
   * Initial state of the ModuleHandler when being created. In this state the
   * handler can be re-configured, started, and its status queried. All other
   * messages to the handler while in this state will cause a <i>Message Not
   * Supported</i> response.
   * @return The Receive implementation for this state.
   */
  def stateInitialize: Receive = {
    case Configure(newSettings) =>
      handlerSettings = newSettings
    case Start =>
      startupHandler()
    case Status =>
      statusRequestHandler(sender)
    case unsupported =>
      unsupportedMsgHandler(sender, unsupported.toString, "Initial")
  }

  /**
   * Running state of the ModuleHandler after being started. In this state the
   * handler can be stopped and its status queried. Stopping the handler will
   * set it back into the <i>Initial</i> state. All messages other than <i>
   * Start</i> and <i>Status</i> to this handler while in <i>Running</i> state
   * will cause a <i>Message Not Supported</i> response.
   * @return The Receive implementation for this state.
   */
  def stateRunning: Receive = {
    case Reinitialize =>
      reinitHandler()
    case Status =>
      statusRequestHandler(sender)
    case Stop =>
      stopHandler()
    case unsupported =>
      unsupportedMsgHandler(sender, unsupported, "Running")
  }


  private def reinitHandler(): Unit = {
    // TODO slk: implement reinitialization procedure
    // Go into state "Initial"
    context become stateInitialize
  }

  private def startupHandler(): Unit = {
    // 1.) Create pool
    //   1.1) Assume RoundRobinPool for now
    //   1.2) Get pool size from ModuleSettings           (workerPoolSize: Int > 0)
    //   1.3) Set a SupervisorStrategy for the workers
    val poolSuper = OneForOneStrategy() { case _ => Resume }
    val pool      = RoundRobinPool(handlerSettings.poolSize, supervisorStrategy = poolSuper)
    // 2.) Create worker props
    //   2.1) Get worker class from ModuleSettings        (workerClass: Class[_]  )
    //   2.2) Get worker parameters from ModuleSettings   (workerParams: Seq[Any] )
    //   2.3) Set mailbox for pool (get default mailbox from master settings -> default to SelectiveMailbox)
    val worker = Props(handlerSettings.workerClass, handlerSettings.workerParams).withMailbox("<<Mailbox String>>")
    // 3.) Create worker pool
    //   3.1) Compose worker pool prop from 'pool' and 'worker' props previously created
    //   3.2) Get module name from ModuleSettings         (name: String           )
    workerPool = Some(context.actorOf(pool.props(worker), handlerSettings.name))
    // 4.) Subscribe worker pool to all configured events (assume default ActorSystem event bus for right now)
    handlerSettings.events.foreach { event =>
      context.system.eventStream.subscribe(workerPool.get, event)
    }
    // Switch handler into state "Running"
    context become stateRunning
  }

  private def statusRequestHandler(origin: ActorRef): Unit = {
    // TODO slk: implement status request-response procedure
    origin ! StatusResponse
  }

  private def stopHandler(): Unit = {
    // TODO slk: implement stopping procedure
  }

  private def unsupportedMsgHandler(origin: ActorRef, message: Any, state: String): Unit = {
    // TODO slk: implement handling unsupported messages
    origin ! MessageNotSupported(message.toString, state)
  }
}

/**
 * Missing documentation.
 *
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
object ModuleHandler {
  /**
   * Default actor properties for ModuleHandler.
   */
  val props = Props[ModuleHandler]

  /**
   * Object containing fault messages the ModuleHandler returns in response
   * to invalid requests sent.
   */
  object Faults {
    case class MessageNotSupported(message: String, state: String)
  }

  /**
   * Object containing all accepted message requests for ModuleHandler
   */
  object Requests {
    case class Configure(settings: ModuleSettings)
    case object Reinitialize
    case object Start
    case object Status
    case object Stop
  }

  /**
   * Object containing all ModuleHandler specific responses that can be
   * returned to a sender
   */
  object Responses {
    case class StatusResponse()
  }
}