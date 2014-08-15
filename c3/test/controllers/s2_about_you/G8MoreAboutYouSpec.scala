package controllers.s2_about_you

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.cache.Cache
import models.domain
import models.domain.Claim
import org.specs2.mutable.{Tags, Specification}
import models.view.CachedClaim

class G8MoreAboutYouSpec extends Specification with Tags {
  "More About You - Controller" should {
    val maritalStatus = "m"

    "make Your Partner Section visible" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("maritalStatus" -> maritalStatus,
          "hadPartnerSinceClaimDate" -> "yes")

      val result = controllers.s2_about_you.G8MoreAboutYou.submit(request)
      val claim = getClaimFromCache(result)

      val section: Section = claim.section(domain.YourPartner)
      section.visible must beTrue
    }

    "hide Your Partner Section" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("maritalStatus" -> maritalStatus,
        "hadPartnerSinceClaimDate" -> "no")

      val result = controllers.s2_about_you.G8MoreAboutYou.submit(request)
      val claim = getClaimFromCache(result)

      val section: Section = claim.section(domain.YourPartner)
      section.visible must beFalse
    }

  } section("unit", models.domain.AboutYou.id)
}