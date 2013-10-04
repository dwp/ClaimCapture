package jmx.claiminspector

import org.specs2.mutable._
import akka.actor._
import org.specs2.time.NoTimeConversions
import scala.concurrent.duration._
import org.joda.time.DateTime
import specs2.akka.AkkaTestkitSpecs2Support

class ClaimInspectorSpec extends Specification with Tags with NoTimeConversions {
  "Claim Inspector" should {
    "accept a timestamped claim" in new AkkaTestkitSpecs2Support {
      within(60 seconds) {
        val actor = system.actorOf(Props[ClaimInspector])
        actor ! ClaimSubmitted(DateTime.now, DateTime.now().plusHours(1))
        actor ! GetClaimStatistics
        expectMsgType[ClaimStatistics] must be equalTo ClaimStatistics(1, 60 * 60)
      }
    }

    "receive fast claim notifications" in new AkkaTestkitSpecs2Support {
      within(60 seconds) {
        val actor = system.actorOf(Props[ClaimInspector])

        for (v <- 0 to 100) {
          actor ! FastClaimDetected
        }

        actor ! GetFastClaimsDetected
        expectMsgType[Int] must be equalTo 101
      }
    }
  } section "unit"
}