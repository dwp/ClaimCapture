package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import models.view.Claiming
import play.api.cache.Cache
import models.domain.{PreviousCarerContactDetails, Claim}
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Section
import scala.Some

class G5PreviousCarerContactDetailsSpec extends Specification with Mockito {

  val previousCarerContactDetailsInput = Seq("address.lineOne" -> "123 Street",
        "postcode" -> "PR2 8AE", 
        "phoneNumber" -> "02076541058",
        "mobileNumber" -> "02076541058")

  "Previous Carer Personal Details - Controller" should {

    "add previous carer personal details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerContactDetailsInput: _*)
        
      val result = controllers.CareYouProvide.previousCarerContactDetailsSubmit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id).get
      
      section.questionGroup(PreviousCarerContactDetails.id) must beLike {
        case Some(f: PreviousCarerContactDetails) => {
          f.address.lineOne mustEqual Some("123 Street")
          f.postcode mustEqual Some("PR2 8AE")
          f.phoneNumber mustEqual Some("02076541058")
          f.phoneNumber mustEqual Some("02076541058")
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = controllers.CareYouProvide.previousCarerContactDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerContactDetailsInput: _*)

      val result = controllers.CareYouProvide.previousCarerContactDetailsSubmit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  

}
