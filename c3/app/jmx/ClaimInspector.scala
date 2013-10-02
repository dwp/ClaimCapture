package jmx

import scala.util.Try
import akka.actor.Actor
import net.sf.ehcache.CacheManager

case class RequestTimeWrapper(path: String, startTime: Long, endTime: Long)

case object GetSessionCount

trait ClaimInspectorMBean extends MBean {
  override def name = "c3:name=ClaimCapture"

  def getSessionCount: Int
}

class ClaimInspector() extends Actor with ClaimInspectorMBean {
  var sessionCount: Int = 0

  override def getSessionCount: Int = {
    sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
    sessionCount
  }

  def setSessionCount(i: Int) = sessionCount = i

  def receive = {
    case GetSessionCount => sender ! getSessionCount
  }
}