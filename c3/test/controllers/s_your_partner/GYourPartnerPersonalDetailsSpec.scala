package controllers.s_your_partner

import models.domain._
import models.{DayMonthYear, NationalInsuranceNumber, domain}
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import controllers.s_about_you.{GMaritalStatus, GNationalityAndResidency}
import models.view.CachedClaim
import utils.WithApplication

class GYourPartnerPersonalDetailsSpec extends Specification {
  val title = "Mr"
  val firstName = "John"
  val middleName = "Mc"
  val surname = "Doe"
  val otherNames = "Duck"
  val nino = "AB123456C"
  val dateOfBirthDay = 10
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990
  val nationality = "British"
  val liveAtSameAddress = "yes"
  val separatedFromPartner = "yes"

  val yourPartnerPersonalDetailsInput = Seq("title" -> title,
    "firstName" -> firstName,
    "middleName" -> middleName,
    "surname" -> surname,
    "otherNames" -> otherNames,
    "nationalInsuranceNumber.nino" -> nino,
    "dateOfBirth.day" -> dateOfBirthDay.toString,
    "dateOfBirth.month" -> dateOfBirthMonth.toString,
    "dateOfBirth.year" -> dateOfBirthYear.toString,
    "partner.nationality" -> nationality,
    "liveAtSameAddress" -> liveAtSameAddress,
    "separated.fromPartner" -> separatedFromPartner,
    "isPartnerPersonYouCareFor" -> "yes",
    "hadPartnerSinceClaimDate" -> "yes")

  val checkForNationalityInput = Seq("title" -> title,
    "firstName" -> firstName,
    "surname" -> surname,
    "dateOfBirth.day" -> dateOfBirthDay.toString,
    "dateOfBirth.month" -> dateOfBirthMonth.toString,
    "dateOfBirth.year" -> dateOfBirthYear.toString,
    "separated.fromPartner" -> separatedFromPartner,
    "isPartnerPersonYouCareFor" -> "yes",
    "hadPartnerSinceClaimDate" -> "yes")

  section("unit", models.domain.YourPartner.id)
  "Your Partner Personal Details - Controller" should {
    "present 'Your Partner Personal Details' " in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GYourPartnerPersonalDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(yourPartnerPersonalDetailsInput: _*)

      val result = GYourPartnerPersonalDetails.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.YourPartner)

      section.questionGroup(YourPartnerPersonalDetails) must beLike {
        case Some(f: YourPartnerPersonalDetails) => {
          f.title must equalTo(Some(title))
          f.firstName must equalTo(Some(firstName))
          f.middleName must equalTo(Some(middleName))
          f.surname must equalTo(Some(surname))
          f.otherSurnames must equalTo(Some(otherNames))
          f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(nino))))
          f.dateOfBirth must equalTo(Some(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None)))
          f.nationality must equalTo(Some(nationality))
          f.separatedFromPartner must equalTo(Some(separatedFromPartner))
        }
      }
    }

    "add submitted form to the cached claim when partner answer is no" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "no")

      val result = GYourPartnerPersonalDetails.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.YourPartner)

      section.questionGroup(YourPartnerPersonalDetails) must beLike {
        case Some(f: YourPartnerPersonalDetails) => {
          f.hadPartnerSinceClaimDate must equalTo("no")
          f.title must equalTo(None)
          f.firstName must equalTo(None)
          f.middleName must equalTo(None)
          f.surname must equalTo(None)
          f.otherSurnames must equalTo(None)
          f.nationalInsuranceNumber must equalTo(None)
          f.dateOfBirth must equalTo(None)
          f.nationality must equalTo(None)
          f.separatedFromPartner must equalTo(None)
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("foo" -> "bar")

      val result = GYourPartnerPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(yourPartnerPersonalDetailsInput: _*)

      val result = GYourPartnerPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "redirect to the next page after a valid submission when partner answer is no" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "no")

      val result = GYourPartnerPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "nationality should be mandatory when carer is not british and married or living with partner" in new WithApplication with Claiming {
      val maritalResult = GMaritalStatus.submit(FakeRequest()
        withFormUrlEncodedBody (
        "maritalStatus" -> "Married or civil partner"
        ))

      status(maritalResult) mustEqual SEE_OTHER

      val result1 = GNationalityAndResidency.submit(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(maritalResult))
        .withFormUrlEncodedBody(
          "nationality" -> "Another nationality",
          "actualnationality" -> "French",
          "resideInUK.answer" -> "yes")
      )

      val result2 = GYourPartnerPersonalDetails.submit(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
        .withFormUrlEncodedBody(checkForNationalityInput: _*))

      status(result2) mustEqual BAD_REQUEST
    }

    "update dp details when partner = dp and partner details are amended" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(yourPartnerPersonalDetailsInput: _*)

      val result = GYourPartnerPersonalDetails.submit(request)
      val claim = getClaimFromCache(result)
      val partner = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
      val dp = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
      dp.title mustEqual (title)
      dp.firstName mustEqual (firstName)
      dp.middleName.getOrElse("") mustEqual (middleName)
      dp.surname mustEqual (surname)
      dp.nationalInsuranceNumber.getOrElse(None) mustEqual (NationalInsuranceNumber(Some(nino)))
      dp.dateOfBirth.`dd/MM/yyyy` mustEqual (s"$dateOfBirthDay/$dateOfBirthMonth/$dateOfBirthYear")
    }
    section("unit", models.domain.YourPartner.id)
  }
}
