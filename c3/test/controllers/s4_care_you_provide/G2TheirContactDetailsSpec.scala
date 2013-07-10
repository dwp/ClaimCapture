package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import play.api.cache.Cache
import models.domain.{Claiming, Claim, Section, TheirContactDetails}
import models.domain
import play.api.test.Helpers._

class G2TheirContactDetailsSpec extends Specification with Mockito with Tags {

  val theirContactDetailsInput = Seq("address.lineOne" -> "123 Street",
    "postcode" -> "PR2 8AE",
    "phoneNumber" -> "02076541058")

  "Their Contact Details - Controller" should {

    "add their contect details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirContactDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G2TheirContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id)

      section.questionGroup(TheirContactDetails) must beLike {
        case Some(f: TheirContactDetails) => {
          f.address.lineOne mustEqual Some("123 Street")
          f.postcode mustEqual Some("PR2 8AE")
          f.phoneNumber mustEqual Some("02076541058")
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = controllers.s4_care_you_provide.G2TheirContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirContactDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G2TheirContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}