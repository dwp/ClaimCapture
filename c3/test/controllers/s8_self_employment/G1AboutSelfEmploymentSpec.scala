package controllers.s8_self_employment

import models.DayMonthYear
import models.domain._
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class G1AboutSelfEmploymentSpec extends Specification with Tags {

  "Self Employment - About Self Employment - Controller" should {
    val areYouSelfEmployedNow = "no"
    val startDay = 11
    val startMonth = 11
    val startYear = 2011
    val finishDay = 11
    val finishMonth = 11
    val finishYear = 2030
    val haveYouCeasedTrading = "yes"
    val natureOfYourBusiness = "Consulting"

    val aboutSelfEmploymentInput = Seq("areYouSelfEmployedNow" -> areYouSelfEmployedNow,
      "whenDidYouStartThisJob.day" -> startDay.toString,
      "whenDidYouStartThisJob.month" -> startMonth.toString,
      "whenDidYouStartThisJob.year" -> startYear.toString,
      "whenDidTheJobFinish.day" -> finishDay.toString,
      "whenDidTheJobFinish.month" -> finishMonth.toString,
      "whenDidTheJobFinish.year" -> finishYear.toString,
      "haveYouCeasedTrading" -> haveYouCeasedTrading,
      "natureOfYourBusiness" -> natureOfYourBusiness
    )

    "present 'About Self Employment' " in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = controllers.s8_self_employment.G1AboutSelfEmployment.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(aboutSelfEmploymentInput: _*)

      val result = controllers.s8_self_employment.G1AboutSelfEmployment.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(AboutSelfEmployment) must beLike {
        case Some(f: AboutSelfEmployment) => {
          f.areYouSelfEmployedNow must equalTo(areYouSelfEmployedNow)
          f.whenDidYouStartThisJob should beLike {
            case dmy: DayMonthYear =>
              dmy.day must equalTo(Some(startDay))
              dmy.month must equalTo(Some(startMonth))
              dmy.year must equalTo(Some(startYear))
          }
          f.whenDidTheJobFinish should beLike {
            case Some(dmy: DayMonthYear) =>
              dmy.day must equalTo(Some(finishDay))
              dmy.month must equalTo(Some(finishMonth))
              dmy.year must equalTo(Some(finishYear))
          }
          f.haveYouCeasedTrading must equalTo(Some(haveYouCeasedTrading))
          f.natureOfYourBusiness must equalTo(Some(natureOfYourBusiness))
        }
      }
    }

    "reject missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("areYouSelfEmployedNow" -> "")

      val result = controllers.s8_self_employment.G1AboutSelfEmployment.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject if areYouSelfEmployedNow answered no but whenDidTheJobFinish not filled in" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("areYouSelfEmployedNow" -> areYouSelfEmployedNow,
        "whenDidYouStartThisJob.day" -> startDay.toString,
        "whenDidYouStartThisJob.month" -> startMonth.toString,
        "whenDidYouStartThisJob.year" -> startYear.toString)

      val result = controllers.s8_self_employment.G1AboutSelfEmployment.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(aboutSelfEmploymentInput: _*)

      val result = controllers.s8_self_employment.G1AboutSelfEmployment.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.SelfEmployment.id)
}