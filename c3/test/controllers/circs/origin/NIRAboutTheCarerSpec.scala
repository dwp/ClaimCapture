package controllers.circs.origin

import controllers.s_care_you_provide.GMoreAboutTheCare
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}

class NIRAboutTheCarerSpec extends Specification {
  section("unit", models.domain.ThirdParty.id)
  "Care about you page " should {
    "show uc question when GB claim" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB"))) {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GMoreAboutTheCare.present(request)
      status(result) mustEqual OK
      contentAsString(result) must contain("Does anyone else spend 35 hours or more each week providing care for")
    }

    "not show uc question when NIR claim" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GMoreAboutTheCare.present(request)
      status(result) mustEqual OK
      contentAsString(result) must not contain ("Does anyone else spend 35 hours or more each week providing care")
    }

    "raise error when submit with no uc question answer in GB claim" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB"))) {
      val moreAboutTheCareInput = Seq("spent35HoursCaring" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(moreAboutTheCareInput: _*)
      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "allow submit with no uc question answer in NIR claim" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) {
      val moreAboutTheCareInput = Seq("spent35HoursCaring" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(moreAboutTheCareInput: _*)
      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.ThirdParty.id)
}
