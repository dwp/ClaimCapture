package jmx

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import org.joda.time.DateTime

class ClaimInspectorSpec extends TestKit(ActorSystem("claimInspectorActorSpec")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
  override def afterAll() {
    system.shutdown()
  }

  val claimInspector = system.actorOf(Props[ClaimInspector])

  "Claim Inspector" must {

    "get current number of sessions" in {
      claimInspector ! GetSessionCount
      expectMsg(0)
    }

    "accept a timestamped claim" in {
      claimInspector ! ClaimSubmitted(DateTime.now, DateTime.now().plusHours(1))
      claimInspector ! GetClaimStatistics
      expectMsg(ClaimStatistics(numberOfClaims = 1, averageTime = 60 * 60))
    }
  }
}