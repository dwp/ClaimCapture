package controllers.s3_your_partner

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Claim

class G3MoreAboutYourPartnerSpec extends Specification {
  val dateDay = 5
  val dateMonth = 12
  val dateYear = 1990
  val separatedFromPartner = "yes"
  
  val moreAboutYourPartnerInput = Seq("dateStartedLivingTogether.day" -> dateDay.toString,
          "dateStartedLivingTogether.month" -> dateMonth.toString,
          "dateStartedLivingTogether.year" -> dateYear.toString,
          "separatedFromPartner" -> separatedFromPartner,
          "separationDate.day" -> dateDay.toString,
          "separationDate.month" -> dateMonth.toString,
          "separationDate.year" -> dateYear.toString)
    
  "More About Your Partner - Controller" should {
    "present 'More About Your Partner'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s3_your_partner.G3MoreAboutYourPartner.present(request)
      status(result) mustEqual OK
    }
    
    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(moreAboutYourPartnerInput: _*)

      val result = controllers.s3_your_partner.G3MoreAboutYourPartner.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.YourPartner.id)

      section.questionGroup(MoreAboutYourPartner) must beLike {
        case Some(f: MoreAboutYourPartner) => {
          f.dateStartedLivingTogether must equalTo(Some(DayMonthYear(Some(dateDay), Some(dateMonth), Some(dateYear), None, None)))
          f.separatedFromPartner must equalTo(separatedFromPartner)
        }
      }
    }
    
    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("foo" -> "bar")

      val result = controllers.s3_your_partner.G3MoreAboutYourPartner.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
    
    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(moreAboutYourPartnerInput: _*)

      val result = controllers.s3_your_partner.G3MoreAboutYourPartner.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
}