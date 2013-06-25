package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import models.view.Claiming
import play.api.cache.Cache
import models.domain.{PreviousCarerPersonalDetails, Claim}
import models.domain
import play.api.test.Helpers._
import models.domain.Section
import scala.Some

class G4PreviousCarerPersonalDetailsSpec extends Specification with Mockito {

  val previousCarerPersonalDetailsInput = Seq("firstName" -> "John", "surname" -> "Doo",
    "dateOfBirth.day" -> "5", "dateOfBirth.month" -> "12", "dateOfBirth.year" -> "1990")

  "Previous Carer Personal Details - Controller" should {

    "add previous carer personal details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerPersonalDetailsInput: _*)
        
      val result = controllers.CareYouProvide.previousCarerPersonalDetailsSubmit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id).get
      
      section.questionGroup(PreviousCarerPersonalDetails.id) must beLike {
        case Some(f: PreviousCarerPersonalDetails) => {
          f.firstName mustEqual Some("John")
          f.surname mustEqual Some("Doo")
          f.dateOfBirth.get.day mustEqual Some(5)
          f.dateOfBirth.get.month mustEqual Some(12)
          f.dateOfBirth.get.year mustEqual Some(1990)
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("dateOfBirth.day" -> "INVALID")

      val result = controllers.CareYouProvide.previousCarerPersonalDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerPersonalDetailsInput: _*)

      val result = controllers.CareYouProvide.previousCarerPersonalDetailsSubmit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  

}
