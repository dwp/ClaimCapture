package controllers

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import models.domain.Claiming
import play.api.test.FakeRequest
import play.api.test.Helpers.OK
import play.api.test.Helpers.status
import play.api.test.WithApplication

class ThankYouSpec extends Specification with Mockito with Tags {
  "Thank You - Controller" should {
    "present 'Thank You' page" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.ThankYou.present("TEST234")(request)
      status(result) mustEqual OK
    }
  } section "unit"
}