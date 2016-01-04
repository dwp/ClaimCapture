package controllers.s_eligibility

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain._
import controllers.s_eligibility
import models.domain.Claim
import models.view.CachedClaim
import utils.WithApplication

class GApproveSpec extends Specification {
  section("unit", models.domain.CarersAllowance.id)
  """Can you get Carer's Allowance""" should {
    "acknowledge that the carer is eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val claim = Claim(CachedClaim.key).update(Benefits(benefitsAnswer = "yes"))
        .update(Eligibility(hours = "yes", over16 = "yes", livesInGB = "yes"))

      cache.set(claimKey, claim)

      val result = s_eligibility.CarersAllowance.approve(request)
      contentAsString(result) must contain("section class=\"prompt e-prompt\"")
    }

    "note that the carer is not eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val claim = Claim(CachedClaim.key).update(Benefits(benefitsAnswer = "yes"))
        .update(Eligibility(hours = "yes", over16 = "no", livesInGB = "yes"))

      cache.set(claimKey, claim)

      val result = s_eligibility.CarersAllowance.approve(request)

      contentAsString(result) must contain("section class=\"prompt e-prompt\"")
    }
  }
  section("unit", models.domain.CarersAllowance.id)
}
