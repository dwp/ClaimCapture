package monitoring

import akka.actor.{Props, ActorSystem, Actor}
import play.api.LoggerLike

class Publisher(logger: LoggerLike) extends Actor {

  val actorSystem = ActorSystem("monitoring-actor-system")

  val cacheMonitor = actorSystem.actorOf(Props[CacheMonitor], name = "cache-monitor")

  val heapMonitor = actorSystem.actorOf(Props(classOf[HeapMonitor], Runtime.getRuntime), name = "heap-monitor")

  def receive: Actor.Receive = {
    case CacheCount =>
      cacheMonitor ! CacheCount
    case HeapStats =>
      heapMonitor ! HeapStats
    case m: Info =>
      logger.info(m.message)
    case m: Warn =>
      logger.warn(m.message)
  }
}

case class Info(message: String)

case class Warn(message: String)
