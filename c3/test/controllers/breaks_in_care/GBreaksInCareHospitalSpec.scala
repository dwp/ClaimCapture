package controllers.breaks_in_care

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings._
import models.DayMonthYear
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.breaks_in_care.GBreaksInCareSummaryPage
import utils.pageobjects.{TestData, PageObjects}
import utils.{WithJsBrowser, WithApplication}

class GBreaksInCareHospitalSpec extends Specification {
  section("unit", models.domain.Breaks.id)
  "Break" should {
    val breakId1 = "1"

    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GBreaksInCareHospital.present("")(request)
      status(result) mustEqual OK
    }

    "Break in care yourStayEnded date by default should not be displayed" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCareSummaryPage(context) goToThePage(throwException = false)
      val data = new TestData
      data.BreaktypeHospitalCheckbox = someTrue.get
      data.BreaktypeOtherYesNo = "no"

      val next = breaksInCare fillPageWith data submitPage()
      next.ctx.browser.click("#whoWasInHospital_You")
      next.ctx.browser.findFirst("#yourStayEnded_date_day").isDisplayed should beFalse
    }

    "Break in care discharged date by default should not be displayed" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCareSummaryPage(context) goToThePage()
      val data = new TestData
      data.BreaktypeHospitalCheckbox = someTrue.get
      data.BreaktypeOtherYesNo = "no"
      val next = breaksInCare fillPageWith data submitPage()
      next.ctx.browser.findFirst("#whenWereYouAdmitted_day").isDisplayed should beFalse
      next.ctx.browser.findFirst("#whenWereYouAdmitted_month").isDisplayed should beFalse
      next.ctx.browser.findFirst("#whenWereYouAdmitted_year").isDisplayed should beFalse
    }

    "Break in care fill data" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCareSummaryPage(context) goToThePage()
      val data = new TestData
      data.BreaktypeHospitalCheckbox = someTrue.get
      data.BreaktypeOtherYesNo = "no"
      val next = breaksInCare fillPageWith data submitPage()

      val sunday = DayMonthYear(7, 6, 2015)

      next.ctx.browser.click("#whoWasInHospital_You")
      next.ctx.browser.fill("#whenWereYouAdmitted_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#whenWereYouAdmitted_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#whenWereYouAdmitted_year") `with` sunday.year.get.toString

      next.ctx.browser.click("#yourStayEnded_answer_yes")
      next.ctx.browser.fill("#yourStayEnded_date_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#yourStayEnded_date_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#yourStayEnded_date_year") `with` sunday.year.get.toString

    }

    "reject when submitted with missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GBreaksInCareHospital.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission your" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "whoWasInHospital" -> BreaksInCareGatherOptions.You,
          "whenWereYouAdmitted.day" -> "1",
          "whenWereYouAdmitted.month" -> "1",
          "whenWereYouAdmitted.year" -> "2001",
          "yourStayEnded.answer" -> "yes",
          "yourStayEnded.date.day" -> "1",
          "yourStayEnded.date.month" -> "1",
          "yourStayEnded.date.year" -> "2001")

      val result = GBreaksInCareHospital.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "reject when submitted year missing data" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "whoWasInHospital" -> BreaksInCareGatherOptions.You,
          "whenWereYouAdmitted.day" -> "1",
          "whenWereYouAdmitted.month" -> "1",
          "whenWereYouAdmitted.year" -> "2001",
          "yourStayEnded.answer" -> "yes",
          "yourStayEnded.date.day" -> "1",
          "yourStayEnded.date.month" -> "1",
          "yourStayEnded.date.year" -> "")

      val result = GBreaksInCareHospital.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page valid DP submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "whoWasInHospital" -> BreaksInCareGatherOptions.DP,
          "whenWasDpAdmitted.day" -> "1",
          "whenWasDpAdmitted.month" -> "1",
          "whenWasDpAdmitted.year" -> "2001",
          "dpStayEnded.answer" -> "yes",
          "dpStayEnded.date.day" -> "1",
          "dpStayEnded.date.month" -> "1",
          "dpStayEnded.date.year" -> "2001",
          "breaksInCareStillCaring" -> "yes"
        )

      val result = GBreaksInCareHospital.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "reject when submitted with breaksInCareStillCaring missing data" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "whoWasInHospital" -> BreaksInCareGatherOptions.DP,
          "whenWasDpAdmitted.day" -> "1",
          "whenWasDpAdmitted.month" -> "1",
          "whenWasDpAdmitted.year" -> "2001",
          "dpStayEnded.answer" -> "yes",
          "dpStayEnded.date.day" -> "1",
          "dpStayEnded.date.month" -> "1",
          "dpStayEnded.date.year" -> "2001",
          "breaksInCareStillCaring" -> ""
        )

      val result = GBreaksInCareHospital.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "add 2 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "whoWasInHospital" -> BreaksInCareGatherOptions.DP,
          "whenWasDpAdmitted.day" -> "1",
          "whenWasDpAdmitted.month" -> "1",
          "whenWasDpAdmitted.year" -> "2001",
          "dpStayEnded.answer" -> "yes",
          "dpStayEnded.date.day" -> "1",
          "dpStayEnded.date.month" -> "1",
          "dpStayEnded.date.year" -> "2001",
          "breaksInCareStillCaring" -> "yes")

      val result = GBreaksInCareHospital.submit(request1)
      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
          "iterationID" -> "2",
          "whoWasInHospital" -> BreaksInCareGatherOptions.DP,
          "whenWasDpAdmitted.day" -> "1",
          "whenWasDpAdmitted.month" -> "1",
          "whenWasDpAdmitted.year" -> "2001",
          "dpStayEnded.answer" -> "yes",
          "dpStayEnded.date.day" -> "1",
          "dpStayEnded.date.month" -> "1",
          "dpStayEnded.date.year" -> "2001",
          "breaksInCareStillCaring" -> "no")

      GBreaksInCareHospital.submit(request2)

      val claim = getClaimFromCache(result)

      claim.questionGroup(BreaksInCare) must beLike { case Some(b: BreaksInCare) => b.breaks.size mustEqual 2 }
    }

    "update existing break" in new WithApplication with Claiming {
      val requestNew = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "whoWasInHospital" -> BreaksInCareGatherOptions.DP,
          "whenWasDpAdmitted.day" -> "1",
          "whenWasDpAdmitted.month" -> "1",
          "whenWasDpAdmitted.year" -> "2001",
          "dpStayEnded.answer" -> "yes",
          "dpStayEnded.date.day" -> "1",
          "dpStayEnded.date.month" -> "1",
          "dpStayEnded.date.year" -> "2001",
          "breaksInCareStillCaring" -> "yes")

      val result = GBreaksInCareHospital.submit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "whoWasInHospital" -> BreaksInCareGatherOptions.DP,
          "whenWasDpAdmitted.day" -> "1",
          "whenWasDpAdmitted.month" -> "1",
          "whenWasDpAdmitted.year" -> "2001",
          "dpStayEnded.answer" -> "yes",
          "dpStayEnded.date.day" -> "1",
          "dpStayEnded.date.month" -> "1",
          "dpStayEnded.date.year" -> yearUpdate.toString,
          "breaksInCareStillCaring" -> "yes")

      GBreaksInCareHospital.submit(requestUpdate)

      getClaimFromCache(result).questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) => b.breaks.head.dpStayEnded.get.date.get.year.get shouldEqual yearUpdate
      }
    }
  }
  section("unit", models.domain.Breaks.id)
}
