package controllers.s2_about_you

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.cache.Cache
import models.domain
import models.domain.Claim
import org.specs2.mutable.{Tags, Specification}
import models.view.CachedDigitalForm

class G5MoreAboutYouSpec extends Specification with Tags {
  "More About You - Controller" should {
    "make Your Partner Section visible" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "yes",
        "beenInEducationSinceClaimDate" -> "yes",
        "receiveStatePension" -> "yes")

      val result = controllers.s2_about_you.G5MoreAboutYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      val section: Section = claim.section(domain.YourPartner)
      section.visible must beTrue
    }

    "hide Your Partner Section" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "no",
        "beenInEducationSinceClaimDate" -> "yes",
        "receiveStatePension" -> "yes")

      val result = controllers.s2_about_you.G5MoreAboutYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      val section: Section = claim.section(domain.YourPartner)
      section.visible must beFalse
    }
    
    "make Education Section visible" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "yes",
        "beenInEducationSinceClaimDate" -> "yes",
        "receiveStatePension" -> "yes")

      val result = controllers.s2_about_you.G5MoreAboutYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      val section: Section = claim.section(domain.Education)
      section.visible mustEqual true
    }

    "hide Education Section" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "yes",
        "beenInEducationSinceClaimDate" -> "no",
        "receiveStatePension" -> "yes")

      val result = controllers.s2_about_you.G5MoreAboutYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      val section: Section = claim.section(domain.Education)
      section.visible mustEqual false
    }
  } section("unit", models.domain.AboutYou.id)
}