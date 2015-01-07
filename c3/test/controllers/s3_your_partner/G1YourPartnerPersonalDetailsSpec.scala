package controllers.s3_your_partner

import models.domain._
import models.{DayMonthYear, NationalInsuranceNumber, domain}
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{WithBrowser, FakeRequest, WithApplication}
import controllers.{Formulate, BrowserMatchers}
import controllers.s2_about_you.G4NationalityAndResidency
import models.view.CachedClaim

class G1YourPartnerPersonalDetailsSpec extends Specification with Tags {
  val title = "Mr"
  val firstName = "John"
  val middleName = "Mc"
  val surname = "Doe"
  val otherNames = "Duck"
  val ni1 = "AB123456C"
  val dateOfBirthDay = 5
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
          "nationalInsuranceNumber.ni1" -> ni1,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> nationality,
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

  "Your Partner Personal Details - Controller" should {
    "present 'Your Partner Personal Details' " in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G1YourPartnerPersonalDetails.present(request)
      status(result) mustEqual OK
    }
    
    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(yourPartnerPersonalDetailsInput: _*)

      val result = G1YourPartnerPersonalDetails.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.YourPartner)

      section.questionGroup(YourPartnerPersonalDetails) must beLike {
        case Some(f: YourPartnerPersonalDetails) => {
          f.title must equalTo(Some(title))
          f.firstName must equalTo(Some(firstName))
          f.middleName must equalTo(Some(middleName))
          f.surname must equalTo(Some(surname))
          f.otherSurnames must equalTo(Some(otherNames))
          f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(ni1))))
          f.dateOfBirth must equalTo(Some(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None)))
          f.nationality must equalTo(Some(nationality))
          f.separatedFromPartner must equalTo(Some(separatedFromPartner))
        }
      }
    }

    "add submitted form to the cached claim when partner answer is no" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "no")

      val result = G1YourPartnerPersonalDetails.submit(request)
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

      val result = G1YourPartnerPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
    
    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(yourPartnerPersonalDetailsInput: _*)

      val result = G1YourPartnerPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "redirect to the next page after a valid submission when partner answer is no" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hadPartnerSinceClaimDate" -> "no")

      val result = G1YourPartnerPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "nationality should be mandatory when carer is not british and married or living with partner" in new WithApplication with Claiming {
      val result1 = G4NationalityAndResidency.submit(FakeRequest()
        withFormUrlEncodedBody(
        "nationality" -> "Another Country",
        "actualnationality" -> "French",
        "resideInUK.answer" -> "yes",
        "maritalStatus" -> "Married or civil partner"))

       val result2 = G1YourPartnerPersonalDetails.submit(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
                     .withFormUrlEncodedBody(checkForNationalityInput:_*))

      status(result2) mustEqual BAD_REQUEST
    }

  }  section("unit", models.domain.YourPartner.id)
}