package monitoring

import akka.actor.{Props, ActorSystem}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.{Play, Logger}
import play.api.Play.current

object ApplicationMonitor {

  val start = Play.configuration.getMilliseconds("monitor.scheduleStart").map(_.milliseconds).getOrElse(30 seconds)
  val every = Play.configuration.getMilliseconds("monitor.scheduleEvery").map(_.milliseconds).getOrElse(15 minutes)

  val actorSystem = ActorSystem("monitoring-actor-system")

  val publisher = actorSystem.actorOf(Props(classOf[Publisher], Logger), name = "publisher")

  def begin = {
    actorSystem.scheduler.schedule(start, every) {
      publisher ! CacheCount
    }
  }

}
