package controllers.save_for_later

import controllers.s_consent_and_declaration.GDeclaration
import models.domain._
import models.view.{CacheHandlingWithClaim, CachedClaim}
import org.specs2.mutable._
import play.api.Play._
import play.api.i18n.Lang
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}
import app.ConfigProperties._
import models.{DayMonthYear, NationalInsuranceNumber}

class GSaveForLaterSpec extends Specification {

  "Save for later controller" should {
    "block submit when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.submit(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "block submit when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.submit(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "present save screen" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.present(request)
      status(result) mustEqual OK
    }

    "allow submit and return save for later success screen" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with Claiming {
      var claim = new Claim(CachedClaim.key, List(), System.currentTimeMillis(), Some(Lang("en")), "123456")
      val details = new YourDetails("", None, "", None, "green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      claim = claim + details
      cache.set("123456", claim)
      val request = FakeRequest().withFormUrlEncodedBody().withSession(CachedClaim.key -> claim.uuid)
      val result = GSaveForLater.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.YourPartner.id)
}
