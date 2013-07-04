package controllers.s3_your_partner

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Claim

class G4PersonYouCareForSpec extends Specification {
  
  val personYouCareForInput = Seq("isPartnerPersonYouCareFor" -> "yes")
    
  "Person You Care For - Controller" should {
    "present 'Person You Care For'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s3_your_partner.G4PersonYouCareFor.present(request)
      status(result) mustEqual OK
    }
    
    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(personYouCareForInput: _*)

      val result = controllers.s3_your_partner.G4PersonYouCareFor.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.YourPartner.id).get

      section.questionGroup(PersonYouCareFor) must beLike {
        case Some(f: PersonYouCareFor) => f.isPartnerPersonYouCareFor must equalTo("yes")
      }
    }
    
    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("isPartnerPersonYouCareFor" -> "INVALID")

      val result = controllers.s3_your_partner.G4PersonYouCareFor.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(personYouCareForInput: _*)

      val result = controllers.s3_your_partner.G4PersonYouCareFor.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
}