package jmx.claiminspector

import org.joda.time.Seconds

class ChangeOfCircsInspector() extends ClaimInspector {
  override def name = "c3:name=ChangeOfCircsCapture"

  override def receive = {
    case ChangeOfCircsSubmitted(start, end) =>
      count = count + 1
      averageTime = (averageTime + Seconds.secondsBetween(start, end).getSeconds) / count

    case GetChangeOfCircsStatistics =>
      sender ! ChangeOfCircsStatistics(count, averageTime)

    case RefererRedirect =>
      refererRedirects = refererRedirects + 1

    case GetRefererRedirects =>
      sender ! getRefererRedirects

    case FastChangeOfCircsDetected =>
      fastCount = fastCount + 1

    case GetFastChangeOfCircsDetected =>
      sender ! getFastCount
  }
}