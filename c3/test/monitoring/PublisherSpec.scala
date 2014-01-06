package monitoring

import org.specs2.mutable.{Tags, Specification}
import specs2.akka.AkkaTestkitSpecs2Support
import org.specs2.time.NoTimeConversions
import akka.actor.Props
import play.api.Logger
import org.specs2.mock.Mockito

class PublisherSpec extends Specification with Tags with NoTimeConversions with Mockito {
  "Publisher" should {
    "respond to CacheCount" in new AkkaTestkitSpecs2Support {
      val logger = mock[Logger]
      val actor = system.actorOf(Props(classOf[Publisher], logger), name = "publisher")
      actor ! CacheCount
      Thread.sleep(1000)
      there was one(logger).info("Cache (Count : 0)")
    }
  } section("unit", "monitoring")
}
