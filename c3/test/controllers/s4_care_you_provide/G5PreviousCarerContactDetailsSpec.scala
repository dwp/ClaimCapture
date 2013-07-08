package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import play.api.cache.Cache
import models.domain.{Claiming, PreviousCarerContactDetails, Claim, Section}
import models.domain
import play.api.test.Helpers._

class G5PreviousCarerContactDetailsSpec extends Specification with Mockito {
  val addressLineOne = "123 Street"
  val postcode = "PR2 8AE"
  val phoneNumber = "02076541058"
  val mobileNumber = "01818118181"

  val previousCarerContactDetailsInput = Seq("address.lineOne" -> addressLineOne,
    "postcode" -> postcode,
    "phoneNumber" -> phoneNumber,
    "mobileNumber" -> mobileNumber)

  "Previous Carer Personal Details - Controller" should {

    "add previous carer personal details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerContactDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G5PreviousCarerContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id)

      section.questionGroup(PreviousCarerContactDetails) must beLike {
        case Some(f: PreviousCarerContactDetails) => {
          f.address.get.lineOne mustEqual Some(addressLineOne)
          f.postcode mustEqual Some(postcode)
          f.phoneNumber mustEqual Some(phoneNumber)
          f.mobileNumber mustEqual Some(mobileNumber)
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = controllers.s4_care_you_provide.G5PreviousCarerContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerContactDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G5PreviousCarerContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }

}
