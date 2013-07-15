package controllers.s2_about_you

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.cache.Cache
import models.domain
import models.domain.Claim
import org.specs2.mutable.{Tags, Specification}

class G5MoreAboutYouSpec extends Specification with Tags {

  "More About You - Controller" should {

   "make Your Partner Section visible" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "yes",
        "eitherClaimedBenefitSinceClaimDate" -> "yes",
        "beenInEducationSinceClaimDate" -> "yes",
        "receiveStatePension" -> "yes")

      val result = controllers.s2_about_you.G5MoreAboutYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      val section: Section = claim.section(domain.YourPartner.id)
      section.visible mustEqual true
    }

    "hide Your Partner Section" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "no",
        "eitherClaimedBenefitSinceClaimDate" -> "yes",
        "beenInEducationSinceClaimDate" -> "yes",
        "receiveStatePension" -> "yes")

      val result = controllers.s2_about_you.G5MoreAboutYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      val section: Section = claim.section(domain.YourPartner.id)
      section.visible mustEqual false
    }
  } section "unit"
}