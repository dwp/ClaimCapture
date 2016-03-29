package controllers.s_self_employment

import models.DayMonthYear
import models.domain._
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.WithApplication

class GAboutSelfEmploymentSpec extends Specification {

  section("unit", models.domain.SelfEmployment.id)
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

    "present 'About Self-Employment' " in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = controllers.s_self_employment.GSelfEmploymentDates.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(aboutSelfEmploymentInput: _*)

      val result = controllers.s_self_employment.GSelfEmploymentDates.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(SelfEmploymentDates) must beLike {
        case Some(f: SelfEmploymentDates) => {
          f.stillSelfEmployed must equalTo(areYouSelfEmployedNow)
          f.startThisWork.getOrElse(None) should beLike {
            case dmy: DayMonthYear =>
              dmy.day must equalTo(Some(startDay))
              dmy.month must equalTo(Some(startMonth))
              dmy.year must equalTo(Some(startYear))
          }
          f.finishThisWork.getOrElse(None) should beLike {
            case Some(dmy: DayMonthYear) =>
              dmy.day must equalTo(Some(finishDay))
              dmy.month must equalTo(Some(finishMonth))
              dmy.year must equalTo(Some(finishYear))
          }
        }
      }
    }

    "reject missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("areYouSelfEmployedNow" -> "")

      val result = controllers.s_self_employment.GSelfEmploymentDates.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject if areYouSelfEmployedNow answered no but whenDidTheJobFinish not filled in" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("areYouSelfEmployedNow" -> areYouSelfEmployedNow,
        "whenDidYouStartThisJob.day" -> startDay.toString,
        "whenDidYouStartThisJob.month" -> startMonth.toString,
        "whenDidYouStartThisJob.year" -> startYear.toString)

      val result = controllers.s_self_employment.GSelfEmploymentDates.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(aboutSelfEmploymentInput: _*)

      val result = controllers.s_self_employment.GSelfEmploymentDates.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.SelfEmployment.id)
}
