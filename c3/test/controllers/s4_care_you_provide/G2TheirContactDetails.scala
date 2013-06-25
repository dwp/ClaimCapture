package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{ WithApplication, FakeRequest }
import models.view.Claiming
import play.api.cache.Cache
import models.domain.{ MoreAboutThePerson, Claim }
import models.domain
import play.api.test.Helpers._
import models.domain.Section
import scala.Some
import models.domain.TheirContactDetails

class G2TheirContactDetails extends Specification with Mockito {

  val theirContactDetailsInput = Seq("address.lineOne" -> "123 Street",
        "postcode" -> "PR2 8AE", 
        "phoneNumber" -> "02076541058")

  
  "Their Contact Details - Controller" should {

    "add their contect details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirContactDetailsInput: _*)

      val result = controllers.CareYouProvide.theirContactDetailsSubmit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id).get

      section.questionGroup(TheirContactDetails.id) must beLike {
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

      val result = controllers.CareYouProvide.theirContactDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirContactDetailsInput: _*)

      val result = controllers.CareYouProvide.theirContactDetailsSubmit(request)
      status(result) mustEqual SEE_OTHER
    }
  }

}