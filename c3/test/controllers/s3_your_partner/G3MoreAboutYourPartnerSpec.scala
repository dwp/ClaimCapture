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

class G3MoreAboutYourPartnerSpec extends Specification {
  val dateStartedLivingTogetherDay = 5
  val dateStartedLivingTogetherMonth = 12
  val dateStartedLivingTogetherYear = 1990
  val separatedFromPartner = "yes"
  
  val moreAboutYourPartnerInput = Seq("dateStartedLivingTogether.day" -> dateStartedLivingTogetherDay.toString,
          "dateStartedLivingTogether.month" -> dateStartedLivingTogetherMonth.toString,
          "dateStartedLivingTogether.year" -> dateStartedLivingTogetherYear.toString,
          "separatedFromPartner" -> separatedFromPartner)
    
  "More About Your Partner - Controller" should {
    "present 'More About Your Partner'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s3_your_partner.G1YourPartnerPersonalDetails.present(request)
      status(result) mustEqual OK
    }
    /*
    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(moreAboutYourPartnerInput: _*)

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
    
    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(yourPartnerPersonalDetailsInput: _*)

      val result = controllers.s3_your_partner.G1YourPartnerPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }*/
  }
}
