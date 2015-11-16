package controllers

import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers.OK
import play.api.test.Helpers.status
import utils.WithApplication
import akka.util.Timeout
import java.util.concurrent.TimeUnit

class ThankYouSpec extends Specification with Mockito {
  implicit val timeout = Timeout(10, TimeUnit.SECONDS)

  "Thank You - Controller" should {
    "present 'Thank You' page for claim" in new WithApplication {
      val request = FakeRequest().withSession("claim" -> claimKey)

      val result = controllers.ClaimEnding.thankyou()(request)
      status(result) mustEqual OK
    }

    "present 'Thank You' page for circs" in new WithApplication {
      val request = FakeRequest().withSession("claim" -> claimKey)

      val result = controllers.CircsEnding.thankyou()(request)
      status(result) mustEqual OK
    }
  }
  section("unit")
}
