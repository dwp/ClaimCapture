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

  def getAverageClaimTime:Int

  def getClaimCount:Int

  def getRefererRedirects:Int

  def getFastClaimsDetected:Int
}

class ClaimInspector() extends Actor with ClaimInspectorMBean {
  var sessionCount: Int = 0

  var claimCount: Int = 0

  var averageClaimTime: Int = 0

  var refererRedirects: Int = 0

  var fastClaimsDetected: Int = 0

  override def getSessionCount: Int = {
    sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
    sessionCount
  }

  override def getAverageClaimTime = averageClaimTime

  override def getClaimCount = claimCount

  override def getRefererRedirects = refererRedirects

  override def setSessionCount(i: Int) = sessionCount = i

  override def getFastClaimsDetected = fastClaimsDetected

  def receive = {
    case GetSessionCount =>
      sender ! getSessionCount

    case ClaimSubmitted(start, end) =>
      claimCount = claimCount + 1
      averageClaimTime = (averageClaimTime + Seconds.secondsBetween(start, end).getSeconds) / claimCount

    case GetClaimStatistics =>
      sender ! ClaimStatistics(claimCount, averageClaimTime)

    case RefererRedirect =>
      refererRedirects = refererRedirects + 1

    case GetRefererRedirects =>
      sender ! getRefererRedirects

    case FastClaimDetected =>
      fastClaimsDetected = fastClaimsDetected + 1

    case GetFastClaimsDetected =>
      sender ! getFastClaimsDetected
  }
}