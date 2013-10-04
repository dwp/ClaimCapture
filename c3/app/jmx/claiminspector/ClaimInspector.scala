package jmx.claiminspector

import akka.actor.Actor
import org.joda.time.Seconds
import jmx.MBean

trait ClaimInspectorMBean extends MBean {
  override def name = "c3:name=ClaimCapture"

  def getCount: Int

  def getFastCount: Int

  def getAverageTime: Int
}

class ClaimInspector() extends Actor with ClaimInspectorMBean {
  var count: Int = 0

  var fastCount: Int = 0

  var averageTime: Int = 0

  override def getCount = count

  override def getFastCount = fastCount

  override def getAverageTime = averageTime

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