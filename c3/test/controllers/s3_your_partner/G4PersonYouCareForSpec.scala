package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.domain._
import models.domain
import play.api.test.Helpers._
import models.domain.Claim
import models.view.CachedClaim

class G4PersonYouCareForSpec extends Specification with Tags {
  
  val personYouCareForInput = Seq("isPartnerPersonYouCareFor" -> "yes")
    
  "Person you care for - Controller" should {
    "present 'Person you care for'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G4PersonYouCareFor.present(request)
      status(result) mustEqual OK
    }
    
    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(personYouCareForInput: _*)

      val result = G4PersonYouCareFor.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.YourPartner)

      section.questionGroup(PersonYouCareFor) must beLike {
        case Some(p: PersonYouCareFor) => p.isPartnerPersonYouCareFor must equalTo("yes")
      }
    }
    
    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("isPartnerPersonYouCareFor" -> "INVALID")

      val result = G4PersonYouCareFor.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(personYouCareForInput: _*)

      val result = G4PersonYouCareFor.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.YourPartner.id)
}