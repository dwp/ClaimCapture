package controllers.breaks_in_care

import app.BreaksInCareOtherOptions
import controllers.mappings.Mappings
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

class GBreaksInCareOtherSpec extends Specification {
  section("unit", models.domain.Breaks.id)
  "Break" should {
    val breakId1 = "1"

    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GBreaksInCareOther.present("")(request)
      status(result) mustEqual OK
    }

    "Break in care start date by default should not be displayed" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCareSummaryPage(context) goToThePage(throwException = false)
      val data = new TestData
      data.BreaktypeNoneCheckbox = someTrue.get
      data.BreaktypeOtherYesNo = "yes"

      val next = breaksInCare fillPageWith data submitPage()
      next.ctx.browser.findFirst("#caringStarted_date_day").isDisplayed should beFalse
    }

    "Break in care fill data" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCareSummaryPage(context) goToThePage()
      val data = new TestData
      data.BreaktypeNoneCheckbox = someTrue.get
      data.BreaktypeOtherYesNo = "yes"
      val next = breaksInCare fillPageWith data submitPage()
      val sunday = DayMonthYear(7, 6, 2015)

      next.ctx.browser.fill("#caringEnded_date_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#caringEnded_date_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#caringEnded_date_year") `with` sunday.year.get.toString

      next.ctx.browser.click("#caringStarted_answer_yes")
      next.ctx.browser.fill("#caringStarted_date_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#caringStarted_date_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#caringStarted_date_year") `with` sunday.year.get.toString
    }

    "reject when submitted with missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GBreaksInCareOther.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission your" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "caringEnded.date.day" -> "1",
          "caringEnded.date.month" -> "1",
          "caringEnded.date.year" -> "2001",
          "caringEnded.time" -> "10",
          "caringStarted.answer" -> Mappings.yes,
          "caringStarted.date.day" -> "1",
          "caringStarted.date.month" -> "1",
          "caringStarted.date.year" -> "2001",
          "caringStarted.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.Home,
          "whereWasDp.text" -> "",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.Holiday,
          "whereWereYou.text" -> "")

      val result = GBreaksInCareOther.submit(request)

      status(result) mustEqual SEE_OTHER
    }

    "reject when submitted year missing data" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "caringEnded.date.day" -> "1",
          "caringEnded.date.month" -> "1",
          "caringEnded.date.year" -> "",
          "caringEnded.time" -> "10",
          "caringStarted.answer" -> Mappings.yes,
          "caringStarted.date.day" -> "1",
          "caringStarted.date.month" -> "1",
          "caringStarted.date.year" -> "2001",
          "caringStarted.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWasDp.text" -> "test1",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test")

      val result = GBreaksInCareOther.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject when submitted with whereWereYou.text missing data" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "caringEnded.date.day" -> "1",
          "caringEnded.date.month" -> "1",
          "caringEnded.date.year" -> "2001",
          "caringEnded.time" -> "10",
          "caringStarted.answer" -> Mappings.yes,
          "caringStarted.date.day" -> "1",
          "caringStarted.date.month" -> "1",
          "caringStarted.date.year" -> "2001",
          "caringStarted.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWasDp.text" -> "test1",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> ""
        )

      val result = GBreaksInCareOther.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "add 2 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "caringEnded.date.day" -> "1",
          "caringEnded.date.month" -> "1",
          "caringEnded.date.year" -> "2001",
          "caringEnded.time" -> "10",
          "caringStarted.answer" -> Mappings.yes,
          "caringStarted.date.day" -> "1",
          "caringStarted.date.month" -> "1",
          "caringStarted.date.year" -> "2001",
          "caringStarted.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWasDp.text" -> "test1",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test")

      val result = GBreaksInCareOther.submit(request1)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
          "iterationID" -> "2",
          "caringEnded.date.day" -> "1",
          "caringEnded.date.month" -> "1",
          "caringEnded.date.year" -> "2001",
          "caringEnded.time" -> "10",
          "caringStarted.answer" -> Mappings.yes,
          "caringStarted.date.day" -> "1",
          "caringStarted.date.month" -> "1",
          "caringStarted.date.year" -> "2001",
          "caringStarted.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWasDp.text" -> "test1",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test")

      GBreaksInCareOther.submit(request2)

      val claim = getClaimFromCache(result)

      claim.questionGroup(BreaksInCare) must beLike { case Some(b: BreaksInCare) => b.breaks.size mustEqual 2 }
    }

    "update existing break" in new WithApplication with Claiming {
      val requestNew = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "caringEnded.date.day" -> "1",
          "caringEnded.date.month" -> "1",
          "caringEnded.date.year" -> "2001",
          "caringEnded.time" -> "10",
          "caringStarted.answer" -> Mappings.yes,
          "caringStarted.date.day" -> "1",
          "caringStarted.date.month" -> "1",
          "caringStarted.date.year" -> "2001",
          "caringStarted.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWasDp.text" -> "test1",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test")

      val result = GBreaksInCareOther.submit(requestNew)

      val yearUpdate = 2000

      val requestUpdate = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "caringEnded.date.day" -> "1",
          "caringEnded.date.month" -> "1",
          "caringEnded.date.year" -> "2001",
          "caringEnded.time" -> "10",
          "caringStarted.answer" -> Mappings.yes,
          "caringStarted.date.day" -> "1",
          "caringStarted.date.month" -> "1",
          "caringStarted.date.year" -> yearUpdate.toString,
          "caringStarted.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.Holiday,
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test"
        )

      val page = GBreaksInCareOther.submit(requestUpdate)

      getClaimFromCache(result).questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) => b.breaks.head.caringStarted.get.date.get.year.get shouldEqual yearUpdate
      }
    }
  }
  section("unit", models.domain.Breaks.id)
}
