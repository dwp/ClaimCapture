package jmx

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import org.specs2.mock.Mockito

class ClaimInspectorSpec extends TestKit(ActorSystem("claimInspectorActorSpec")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    system.shutdown()
  }

  "Claim Inspector" must {
    "get current number of sessions" in {

      val claimInspector = system.actorOf(Props[ClaimInspector])

      claimInspector ! GetSessionCount

      expectMsg(0)
    }
  }
}
