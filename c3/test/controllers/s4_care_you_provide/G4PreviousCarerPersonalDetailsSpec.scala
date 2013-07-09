package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import play.api.cache.Cache
import models.domain.{Claiming, PreviousCarerPersonalDetails, Claim, Section}
import models.{DayMonthYear, domain}
import play.api.test.Helpers._

class G4PreviousCarerPersonalDetailsSpec extends Specification with Mockito with Tags {
  val firstName = "John"
  val surname = "Doe"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990

  val previousCarerPersonalDetailsInput = Seq("firstName" -> "John", "surname" -> surname,
    "dateOfBirth.day" -> dateOfBirthDay.toString, "dateOfBirth.month" -> dateOfBirthMonth.toString, "dateOfBirth.year" -> dateOfBirthYear.toString)

  "Previous Carer Personal Details - Controller" should {

    "add previous carer personal details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerPersonalDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G4PreviousCarerPersonalDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id)

      section.questionGroup(PreviousCarerPersonalDetails) must beLike {
        case Some(f: PreviousCarerPersonalDetails) => {
          f.firstName mustEqual Some(firstName)
          f.surname mustEqual Some(surname)
          f.dateOfBirth mustEqual Some(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None))
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("dateOfBirth.day" -> "INVALID")

      val result = controllers.s4_care_you_provide.G4PreviousCarerPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(previousCarerPersonalDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G4PreviousCarerPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}