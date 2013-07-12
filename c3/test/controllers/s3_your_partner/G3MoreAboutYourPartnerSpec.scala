package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Claim

class G3MoreAboutYourPartnerSpec extends Specification with Tags {
  val dateDay = 5
  val dateMonth = 12
  val dateYear = 1990
  val yes = "yes"
  
  val moreAboutYourPartnerInput = Seq("startedLivingTogether.afterClaimDate" -> yes,
          "startedLivingTogether.date.day" -> dateDay.toString,
          "startedLivingTogether.date.month" -> dateMonth.toString,
          "startedLivingTogether.date.year" -> dateYear.toString,
          "separated.fromPartner" -> yes,
          "separated.date.day" -> dateDay.toString,
          "separated.date.month" -> dateMonth.toString,
          "separated.date.year" -> dateYear.toString)
    
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
          f.startedLivingTogether.get.answer must equalTo(yes)
          f.startedLivingTogether.get.date must equalTo(Some(DayMonthYear(Some(dateDay), Some(dateMonth), Some(dateYear), None, None)))
          f.separated.answer must equalTo(yes)
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
  } section "unit"
}