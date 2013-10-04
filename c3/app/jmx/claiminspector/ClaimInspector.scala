package jmx.claiminspector

import akka.actor.Actor
import org.joda.time.Seconds
import jmx.MBean

trait ClaimInspectorMBean extends MBean {
  override def name = "c3:name=ClaimCapture"

  def getAverageTime: Int

  def getCount: Int

  def getFastCount: Int
}

class ClaimInspector() extends Actor with ClaimInspectorMBean {
  var count: Int = 0

  var fastCount: Int = 0

  var averageTime: Int = 0

  override def getAverageTime = averageTime

  override def getCount = count

  override def getFastCount = fastCount

  def receive = {
    case ClaimSubmitted(start, end) =>
      count = count + 1
      averageTime = (averageTime + Seconds.secondsBetween(start, end).getSeconds) / count

    case GetClaimStatistics =>
      sender ! ClaimStatistics(count, averageTime)

    case FastClaimDetected =>
      fastCount = fastCount + 1

    case GetFastClaimsDetected =>
      sender ! getFastCount
  }
}