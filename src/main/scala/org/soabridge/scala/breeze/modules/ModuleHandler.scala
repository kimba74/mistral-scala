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
   *   - The unique module name        (String)
   *   - Pool size                     (Integer)
   *   - The worker actor class        (Class[_] or Class[_ <: Actor])
   *   - The worker actor's parameters (Seq[Any])
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

  /** */
  def receive: Receive = stateInitial

  /**
   * Initial state of the ModuleHandler when being created. In this state the
   * handler can be re-configured, started, and its status queried. All other
   * messages to the handler while in this state will cause a <i>Message Not
   * Supported</i> response.
   * @return The Receive implementation for this state.
   */
  def stateInitial: Receive = {
    case Configure(newSettings) =>
      handlerSettings = newSettings
    case Start =>
      startupHandler()
      context become stateRunning
    case Status =>
      sender ! createStatusResponse()
    case unsupported =>
      sender ! MessageNotSupported(unsupported.toString, "Initial")
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
    case Status =>
      sender ! createStatusResponse()
    case Stop =>
      stopHandler()
      context become stateInitial
    case unsupported =>
      sender ! MessageNotSupported(unsupported.toString, "Running")
  }


  private def createStatusResponse(): StatusResponse = {
    // TODO slk: figure out what is being returned as status request
    StatusResponse
  }

  private def startupHandler(): Unit = {
    // 1.) create worker pool (assume RoundRobinPool for now; get size from ModuleSettings)
    //   1.1) Assume RoundRobinPool for now
    //   1.2) Get pool size from ModuleSettings         (workerPoolSize: Int > 0)
    val pool  = RoundRobinPool(handlerSettings.workerPoolSize)
    //   1.2) Get worker class from ModuleSettings      (workerClass: Class[_]  )
    //   1.3) Get worker parameters from ModuleSettings (workerParams: Seq[Any] )
    //   1.4) Set mailbox for pool (get default mailbox from master settings -> default to SelectiveMailbox)
    val props = Props(handlerSettings.workerClass, handlerSettings.workerParams).withMailbox("<<Mailbox String>>")
    //   1.5) Get module name from ModuleSettings       (name: String           )
    workerPool = Some(context.actorOf(pool.props(props), handlerSettings.name))
    // 2.) Subscribe worker pool to event bus        (assume default ActorSystem event bus for right now)
    // TODO slk: subscribe Worker Pool to EventBus
  }

  private def stopHandler(): Unit = {
    // TODO slk: implement stopping procedure
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