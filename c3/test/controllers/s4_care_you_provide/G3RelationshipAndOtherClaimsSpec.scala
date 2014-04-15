package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import play.api.cache.Cache
import models.domain.{Claiming, MoreAboutThePerson, Claim, Section}
import models.domain
import play.api.test.Helpers._
import models.view.CachedClaim

class G3RelationshipAndOtherClaimsSpec extends Specification with Mockito with Tags {

  val moreAboutThePersonInput = Seq("relationship" -> "father", "armedForcesPayment" -> "yes")

  "More About The Person - Controller" should {

    "present 'More About The Person' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G3RelationshipAndOtherClaims.present(request)
      status(result) mustEqual OK
    }

    "add more about the person details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(moreAboutThePersonInput: _*)

      val result = G3RelationshipAndOtherClaims.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide)

      section.questionGroup(MoreAboutThePerson) must beLike {
        case Some(m: MoreAboutThePerson) => {
          m.relationship mustEqual "father"
          m.armedForcesPayment mustEqual "yes"
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("relationship" -> "")

      val result = G3RelationshipAndOtherClaims.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(moreAboutThePersonInput: _*)

      val result = G3RelationshipAndOtherClaims.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.CareYouProvide.id)
}