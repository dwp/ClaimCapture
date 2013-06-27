package controllers.s4_care_you_provide

import play.api.test.{FakeRequest, WithApplication}
import models.view.Claiming
import play.api.test.Helpers._
import org.specs2.mutable.Specification
import play.api.cache.Cache
import models.domain.{OneWhoPaysPersonalDetails, Section, Claim}
import models.domain

class G8OneWhoPaysPersonalDetailsSpec extends Specification {


  "G8OneWhoPaysPersonalDetails - Controller" should {

    "add 'one who pays personal details' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("firstName" -> "John")

      val result = controllers.s4_care_you_provide.G8OneWhoPaysPersonalDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id).get

      section.questionGroup(OneWhoPaysPersonalDetails.id) must beLike {
        case Some(f: OneWhoPaysPersonalDetails) => {
          f.firstName mustEqual Some("John")
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("startDatePayment.year" -> "12345")

      val result = controllers.s4_care_you_provide.G8OneWhoPaysPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("" -> "")

      val result = controllers.s4_care_you_provide.G8OneWhoPaysPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  }
}
