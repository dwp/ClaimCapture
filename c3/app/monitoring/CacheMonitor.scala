package monitoring

import akka.actor.Actor
import scala.util.Try
import net.sf.ehcache.CacheManager

class CacheMonitor extends Actor {
  def receive: Actor.Receive = {
    case CacheCount =>
      val cacheCount = Try(CacheManager.getInstance().getCache("play").getKeysWithExpiryCheck.size()).getOrElse(0)
      sender ! Info(s"Cache (Count : $cacheCount)")
  }
}

case class CacheCount()
