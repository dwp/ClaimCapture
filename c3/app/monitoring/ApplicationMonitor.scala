package monitoring

import akka.actor.{Props, ActorSystem}
import app.ConfigProperties._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.Logger

object ApplicationMonitor {

  val start: Long = getProperty("monitor.scheduleStart", 30)
  val every: Long = getProperty("monitor.scheduleEvery", 30)
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
