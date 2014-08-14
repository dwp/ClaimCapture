package controllers.s2_about_you

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.cache.Cache
import models.domain
import models.domain.Claim
import org.specs2.mutable.{Tags, Specification}
import models.view.CachedClaim
import app.MaritalStatus

class G8MoreAboutYouSpec extends Specification with Tags {
  "More About You - Controller" should {
    val maritalStatus = MaritalStatus.Partner

    "marital status is added to the form on submit" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("maritalStatus" -> maritalStatus)

      val result = controllers.s2_about_you.G8MoreAboutYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(MoreAboutYou) must beLike {
        case Some(o: MoreAboutYou) => {
          o.maritalStatus shouldEqual maritalStatus
        }
      }
    }

  } section("unit", models.domain.AboutYou.id)
}