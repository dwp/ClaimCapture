package controllers.circs.origin

import controllers.preview.Preview
import controllers.s_care_you_provide.GMoreAboutTheCare
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.preview.PreviewPage
import utils.{LightFakeApplication, WithApplication}

class NIRAboutTheCarerPreviewSpec extends Specification {
  section("unit", models.domain.ThirdParty.id)
  "Care about you preview page" should {
    "show uc question in preview for GB claim" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB"))) {
      val moreAboutTheCareInput = Seq("spent35HoursCaring" -> "no", "otherCarer" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(moreAboutTheCareInput: _*)
      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual SEE_OTHER

      val preview=Preview.present(request)
      contentAsString(preview) must contain("Do you spend 35 hours or more each week providing care for")
      contentAsString(preview) must contain("Does anyone else spend 35 hours or more each week providing care for")
    }

    "NOT show uc question in preview for NIR claim" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) {
      val moreAboutTheCareInput = Seq("spent35HoursCaring" -> "no", "otherCarer" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(moreAboutTheCareInput: _*)
      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual SEE_OTHER

      val preview=Preview.present(request)
      contentAsString(preview) must contain("Do you spend 35 hours or more each week providing care for")
      contentAsString(preview) must not contain("Does anyone else spend 35 hours or more each week providing care for")
    }
  }
  section("unit", models.domain.ThirdParty.id)
}
