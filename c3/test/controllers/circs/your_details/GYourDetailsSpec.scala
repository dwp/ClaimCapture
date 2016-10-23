package controllers.circs.your_details

import controllers.circs.start_of_process
import controllers.circs.start_of_process.GReportChangeReason
import controllers.mappings.Mappings
import models.domain._
import models.view.CachedChangeOfCircs
import models.{DayMonthYear, NationalInsuranceNumber}
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GYourDetailsSpec extends Specification{

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Circumstances - About You - Controller" should {
    val firstName="John"
    val surname="Smith"
    val nino = "AB123456C"
    val dateOfBirthDay = 5
    val dateOfBirthMonth = 12
    val dateOfBirthYear = 1990
    val theirFirstName = "Jane"
    val theirSurname = "Jones"
    val theirRelationshipToYou = "Wife"

    val aboutYouInput = Seq(
      "firstName" -> firstName,
      "surname" -> surname,
      "nationalInsuranceNumber.nino" -> nino.toString,
      "dateOfBirth.day" -> dateOfBirthDay.toString,
      "dateOfBirth.month" -> dateOfBirthMonth.toString,
      "dateOfBirth.year" -> dateOfBirthYear.toString,
      "theirFirstName" -> theirFirstName,
      "theirSurname" -> theirSurname,
      "theirRelationshipToYou" -> theirRelationshipToYou,
      "furtherInfoContact"->"01234567890",
      "wantsEmailContactCircs"->"no"
    )

    //CachedChangeOfCircs.key
    
    "present 'Circumstances About You' " in new WithApplication with MockForm {
      val request = FakeRequest()

      val result = GYourDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(aboutYouInput: _*)

      val result = GYourDetails.submit(request)
      val claim = getClaimFromCache(result,CachedChangeOfCircs.key)
      val section: Section = claim.section(models.domain.CircumstancesIdentification)
      section.questionGroup(CircumstancesYourDetails) must beLike {
        case Some(f: CircumstancesYourDetails) => {
          f.firstName must equalTo("John")
          f.surname must equalTo("Smith")
          f.nationalInsuranceNumber must equalTo(NationalInsuranceNumber(Some(nino)))
          f.dateOfBirth must equalTo(DayMonthYear(dateOfBirthDay, dateOfBirthMonth, dateOfBirthYear))
        }
      }
    }

    "missing mandatory field" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody("firstName" -> "")

      val result = GYourDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(aboutYouInput: _*)

      val result = GYourDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """should be valid submission with no contact number supplied".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "wantsEmailContactCircs"-> Mappings.no)

      val result = GYourDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """return bad request with characters in contact number".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "furtherInfoContact"->"dsdhjsdjs",
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "wantsEmailContactCircs"-> Mappings.no)

      val result = GYourDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """return bad request with less than minimum digits entered".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "furtherInfoContact"->"012345",
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "wantsEmailContactCircs"-> Mappings.no)

      val result = GYourDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """return bad request with greater than maximum digits entered".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "furtherInfoContact"->"012345678901234567890",
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "wantsEmailContactCircs"-> Mappings.no)

      val result = GYourDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "when present called Lang should not be set on Claim" in new WithApplication with MockForm {
      val request = FakeRequest().withHeaders(REFERER -> "http://localhost:9000/circumstances/identification/about-you")

      val result = GYourDetails.present(request)
      val claim = getClaimFromCache(result,CachedChangeOfCircs.key)
      claim.lang mustEqual None
    }

    """should be valid submission with no contact number supplied".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "wantsEmailContactCircs"-> Mappings.no)

      val result = GYourDetails.submit(request)
      getClaimFromCache(result,CachedChangeOfCircs.key).questionGroup(CircumstancesYourDetails) should beLike {
        case Some(f: CircumstancesYourDetails) => f.wantsContactEmail shouldEqual "no"; f.email shouldEqual None
      }
      val claim = getClaimFromCache(result,CachedChangeOfCircs.key)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
