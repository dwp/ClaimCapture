package controllers.s4_care_you_provide

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}
import play.api.cache.Cache
import models.domain.{Claiming, OneWhoPaysPersonalDetails, Section, Claim}
import models.domain

class G8OneWhoPaysPersonalDetailsSpec extends Specification with Tags {

  "G8OneWhoPaysPersonalDetails - Controller" should {

    "add 'one who pays personal details' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("firstName" -> "John")

      val result = G8OneWhoPaysPersonalDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide)

      section.questionGroup(OneWhoPaysPersonalDetails) must beLike {
        case Some(o: OneWhoPaysPersonalDetails) => {
          o.firstName mustEqual Some("John")
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("startDatePayment.year" -> "12345")

      val result = G8OneWhoPaysPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("" -> "")

      val result = G8OneWhoPaysPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}