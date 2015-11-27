package controllers.save_for_later

import controllers.s_consent_and_declaration.GDeclaration
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.Play._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}
import app.ConfigProperties._

class GSaveForLaterSpec extends Specification {

  "Save for later controller" should {
    "present save screen" in new WithApplication with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.present(request)
      status(result) mustEqual OK
    }
    
    "allow submit" in new WithApplication with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.submit(request)
      status(result) mustEqual OK
    }

    "block submit when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.submit(request)
     status(result) mustEqual SEE_OTHER
    }

  }
  section("unit", models.domain.YourPartner.id)
}
