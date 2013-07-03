package controllers.s3_your_partner

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import models.view.Claiming
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Section
import scala.Some
import models.NationalInsuranceNumber

class G1YourPartnerPersonalDetailsSpec extends Specification {
/*
  val theirPersonalDetailsInput = Seq("title" -> "Mr", "firstName" -> "John", "surname" -> "Doo",
    "dateOfBirth.day" -> "5", "dateOfBirth.month" -> "12", "dateOfBirth.year" -> "1990", "liveAtSameAddress" -> "yes")

  "Their Personal Details - Controller" should {







    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirPersonalDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G1TheirPersonalDetails.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/theirContactDetails")
    }
  }

*/
  val title = "Mr"
  val firstName = "John"
  val middleName = "Mc"
  val surname = "Doe"
  val otherNames = "Duck"
  val ni1 = "AB"
  val ni2 = 12
  val ni3 = 34
  val ni4 = 56
  val ni5 = "C"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990
  val nationality = "British"
  val liveAtSameAddress = "yes"
  
  val yourPartnerPersonalDetailsInput = Seq("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> nationality,
          "liveAtSameAddress" -> liveAtSameAddress)
    
  "Your Partner Personal Details - Controller" should {
    "present 'Your Partner Personal Details' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s3_your_partner.G1YourPartnerPersonalDetails.present(request)
      status(result) mustEqual OK
    }
    
    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(yourPartnerPersonalDetailsInput: _*)

      val result = controllers.s3_your_partner.G1YourPartnerPersonalDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.YourPartner.id).get

      section.questionGroup(YourPartnerPersonalDetails.id) must beLike {
        case Some(f: YourPartnerPersonalDetails) => {
          f.title must equalTo(title)
          f.firstName must equalTo(firstName)
          f.middleName must equalTo(Some(middleName))
          f.surname must equalTo(surname)
          f.otherNames must equalTo(Some(otherNames))
          f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(ni1), Some(ni2.toString), Some(ni3.toString), Some(ni4.toString), Some(ni5))))
          f.dateOfBirth must equalTo(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None))
          f.nationality must equalTo(Some(nationality))
          f.liveAtSameAddress must equalTo(liveAtSameAddress)
        }
      }
    }
    
    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("foo" -> "bar")

      val result = controllers.s3_your_partner.G1YourPartnerPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  }
}
