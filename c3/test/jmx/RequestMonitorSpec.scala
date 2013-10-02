package jmx

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.mvc.RequestHeader
import org.specs2.mock.Mockito
import org.joda.time.DateTime

class RequestMonitorSpec extends TestKit(ActorSystem("jmx-actor-system")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll with Mockito {
  override def afterAll() {
    system.shutdown()
  }

  "Request" must {
    "record a request and its time" in {
      val actor = system.actorOf(Props[RequestMonitor])

      val requestDetails = RequestDetails(mock[RequestHeader], DateTime.now(), DateTime.now().plusSeconds(2))
      actor ! requestDetails

      actor ! GetRequestDetails
      expectMsg(List(requestDetails))
    }
  }
}
