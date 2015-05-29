package controllers.s4_care_you_provide

import models.domain
import models.domain.{Claiming, Section, TheirContactDetails}
import org.specs2.mock.Mockito
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.WithApplication

class G2TheirContactDetailsSpec extends Specification with Mockito with Tags {

  val theirContactDetailsInput = Seq("address.lineOne" -> "123 Street",
    "address.lineTwo" -> "Preston",
    "postcode" -> "PR2 8AE")

  "Their Contact Details - Controller" should {

    "add their contect details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(theirContactDetailsInput: _*)

      val result = G2TheirContactDetails.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.CareYouProvide)

      section.questionGroup(TheirContactDetails) must beLike {
        case Some(t: TheirContactDetails) => {
          t.address.lineOne mustEqual Some("123 Street")
          t.postcode mustEqual Some("PR2 8AE")
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = G2TheirContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(theirContactDetailsInput: _*)

      val result = G2TheirContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.CareYouProvide.id)
}