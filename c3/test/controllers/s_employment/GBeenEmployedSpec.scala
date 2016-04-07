package controllers.s_employment

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain._
import models.domain.YourIncomes
import controllers.mappings.Mappings._
import models.view.CachedClaim
import utils.WithApplication

class GBeenEmployedSpec extends Specification {
  section("unit", models.domain.BeenEmployed.id)
  "Been Employed" should {
    "first present job details" in new WithApplication with Claiming {
      val claim = Claim(CachedClaim.key)
        .update(ClaimDate())
        .update(YourIncomes(beenEmployedSince6MonthsBeforeClaim = yes))

      cache.set("default"+claimKey, claim)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GBeenEmployed.present(request)
      status(result) mustEqual SEE_OTHER
    }

    "submit no data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GBeenEmployed.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "submit been employed" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("beenEmployed" -> "yes")
      val result = GBeenEmployed.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "submit not been employed" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("beenEmployed" -> "no")
      val result = GBeenEmployed.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "submit with bad data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("beenEmployed" -> "asdf")
      val result = GBeenEmployed.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  }
  section("unit", models.domain.BeenEmployed.id)
}
