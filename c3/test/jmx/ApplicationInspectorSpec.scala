package jmx

import org.specs2.mutable._
import akka.actor._
import org.specs2.time.NoTimeConversions
import scala.concurrent.duration._
import specs2.akka.AkkaTestkitSpecs2Support
import jmx.inspectors.{GetRefererRedirects, RefererRedirect, GetSessionCount, ApplicationInspector}

class ApplicationInspectorSpec extends Specification with Tags with NoTimeConversions {
  "Application Inspector" should {
    "get current number of sessions" in new AkkaTestkitSpecs2Support {
      within(60 seconds) {
        system.actorOf(Props[ApplicationInspector]) ! GetSessionCount
        expectMsgType[Int] must be equalTo 0
      }
    }

    "receive a referer redirect" in new AkkaTestkitSpecs2Support {
      within(60 seconds) {
        val actor = system.actorOf(Props[ApplicationInspector])
        actor ! RefererRedirect
        actor ! RefererRedirect
        actor ! GetRefererRedirects
        expectMsgType[Int] must be equalTo 2
      }
    }
  } section ("unit","jmx")
}