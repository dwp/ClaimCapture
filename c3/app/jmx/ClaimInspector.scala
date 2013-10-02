package jmx

import scala.util.Try
import akka.actor.Actor
import net.sf.ehcache.CacheManager
import org.joda.time.{Seconds, DateTime}

case class ClaimSubmitted(start: DateTime, end: DateTime)

case object GetClaimStatistics

case class ClaimStatistics(numberOfClaims: Int, averageTime: Int)

case object GetSessionCount

trait ClaimInspectorMBean extends MBean {
  override def name = "c3:name=ClaimCapture"

  def getSessionCount: Int

  def setSessionCount(i: Int): Unit
}

class ClaimInspector() extends Actor with ClaimInspectorMBean {
  var sessionCount: Int = 0

  var claimCount: Int = 0

  var averageClaimTime: Int = 0

  override def getSessionCount: Int = {
    sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
    sessionCount
  }

  override def setSessionCount(i: Int) = sessionCount = i

  def receive = {
    case GetSessionCount =>
      sender ! getSessionCount

    case ClaimSubmitted(start,end) =>
      claimCount = claimCount + 1
      averageClaimTime = (averageClaimTime + Seconds.secondsBetween(start, end).getSeconds) / claimCount

    case GetClaimStatistics =>
      sender ! ClaimStatistics(claimCount, averageClaimTime)
  }
}