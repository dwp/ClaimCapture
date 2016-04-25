package controllers.circs.origin

import models.domain.{Claiming}
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}

class GOriginSpec extends Specification {
  val inputCarer = Seq("thirdParty" -> "yesCarer")
  val inputNoCarerOrganisation = Seq("thirdParty" -> "noCarer", "thirdParty.nameAndOrganisation" -> "Test")

  section("unit", models.domain.ThirdParty.id)
  "Origin Page" should {
    "present origin country select page for origin=GB-NIR" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GOrigin.present(request)
      status(result) mustEqual OK
    }

    "redirect to Circs for origin=GB-NIR" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB"))) with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GOrigin.present(request)
      status(result) mustEqual SEE_OTHER
      redirectLocation(result) must beSome("/circumstances/report-changes/change-selection")
    }

    "block submit for no country selection" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB"))) with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GOrigin.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  }
  section("unit", models.domain.ThirdParty.id)
}
