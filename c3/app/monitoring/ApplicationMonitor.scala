package monitoring

import akka.actor.{Props, ActorSystem}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.{Play, Logger}
import play.api.Play.current

object ApplicationMonitor {

  val start: Long = Play.configuration.getLong("monitor.scheduleStart").getOrElse(10)
  val every: Long = Play.configuration.getLong("monitor.scheduleEvery").getOrElse(10)
  val startDuration = Duration(start, MINUTES)
  val endDuration = Duration(every, MINUTES)
  val actorSystem = ActorSystem("monitoring-actor-system")

  val publisher = actorSystem.actorOf(Props(classOf[Publisher], Logger), name = "publisher")

  def begin = {
    actorSystem.scheduler.schedule(startDuration, endDuration) {
      publisher ! CacheCount
      publisher ! HeapStats
    }
  }

}
