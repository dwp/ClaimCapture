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

  def getAverageClaimTime: Int

  def getAverageChangeOfCircsTime: Int

  def getClaimCount: Int

  def getChangeOfCircsCount: Int

  def getFastClaimsDetected: Int

  def getFastChangeOfCircsDetected: Int
}

class ClaimInspector() extends Actor with ClaimInspectorMBean {
  var sessionCount: Int = 0

  var fastClaimsDetected: Int = 0

  var claimCount: Int = 0

  var changeOfCircsCount: Int = 0

  var averageClaimTime: Int = 0

  var averageChangeOfCircsTime: Int = 0

  var refererRedirects: Int = 0

  var fastChangeOfCircsDetected: Int = 0

  override def getSessionCount: Int = {
    sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
    sessionCount
  }

  override def setSessionCount(i: Int) = sessionCount = i

  override def getRefererRedirects = refererRedirects

  override def getAverageClaimTime = averageClaimTime

  override def getAverageChangeOfCircsTime = averageChangeOfCircsTime

  override def getClaimCount = claimCount

  override def getChangeOfCircsCount = changeOfCircsCount

  override def getFastClaimsDetected = fastClaimsDetected

  override def getFastChangeOfCircsDetected = fastChangeOfCircsDetected

  def receive = {
    case GetSessionCount =>
      sender ! getSessionCount

    case ClaimSubmitted(start, end) =>
      claimCount = claimCount + 1
      averageClaimTime = (averageClaimTime + Seconds.secondsBetween(start, end).getSeconds) / claimCount

    case ChangeOfCircsSubmitted(start, end) =>
      changeOfCircsCount = changeOfCircsCount + 1
      averageChangeOfCircsTime = (averageChangeOfCircsTime + Seconds.secondsBetween(start, end).getSeconds) / changeOfCircsCount

    case GetClaimStatistics =>
      sender ! ClaimStatistics(claimCount, averageClaimTime)

    case GetChangeOfCircsStatistics =>
      sender ! ChangeOfCircsStatistics(changeOfCircsCount, averageChangeOfCircsTime)

    case RefererRedirect =>
      refererRedirects = refererRedirects + 1

    case GetRefererRedirects =>
      sender ! getRefererRedirects

    case FastClaimDetected =>
      fastClaimsDetected = fastClaimsDetected + 1

    case FastChangeOfCircsDetected =>
      fastChangeOfCircsDetected = fastChangeOfCircsDetected + 1

    case GetFastClaimsDetected =>
      sender ! getFastClaimsDetected

    case GetFastChangeOfCircsDetected =>
      sender ! getFastChangeOfCircsDetected
  }
}