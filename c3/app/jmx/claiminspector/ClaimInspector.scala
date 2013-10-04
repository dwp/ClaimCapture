package jmx.claiminspector

import scala.util.Try
import akka.actor.Actor
import net.sf.ehcache.CacheManager
import org.joda.time.Seconds
import jmx.MBean

trait ClaimInspectorMBean extends MBean {
  override def name = "c3:name=ClaimCapture"

  def getSessionCount: Int

  def setSessionCount(i: Int): Unit

  def getRefererRedirects: Int

  def getAverageTime: Int

  def getCount: Int

  def getFastCount: Int
}

class ClaimInspector() extends Actor with ClaimInspectorMBean {
  var sessionCount: Int = 0

  var refererRedirects: Int = 0

  var count: Int = 0

  var fastCount: Int = 0

  var averageTime: Int = 0

  override def getSessionCount: Int = {
    sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
    sessionCount
  }

  override def setSessionCount(i: Int) = sessionCount = i

  override def getRefererRedirects = refererRedirects

  override def getAverageTime = averageTime

  override def getCount = count

  override def getFastCount = fastCount

  def receive = {
    case GetSessionCount =>
      sender ! getSessionCount

    case ClaimSubmitted(start, end) =>
      count = count + 1
      averageTime = (averageTime + Seconds.secondsBetween(start, end).getSeconds) / count

    case GetClaimStatistics =>
      sender ! ClaimStatistics(count, averageTime)

    case RefererRedirect =>
      refererRedirects = refererRedirects + 1

    case GetRefererRedirects =>
      sender ! getRefererRedirects

    case FastClaimDetected =>
      fastCount = fastCount + 1

    case GetFastClaimsDetected =>
      sender ! getFastCount

  }
}