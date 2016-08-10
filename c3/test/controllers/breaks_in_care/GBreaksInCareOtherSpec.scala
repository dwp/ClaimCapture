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
import utils.pageobjects.breaks_in_care.GBreaksInCareTypePage
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
      val breaksInCare = GBreaksInCareTypePage(context) goToThePage(throwException = false)
      val data = new TestData
      data.BreaktypeNoneCheckbox = someTrue.get
      data.BreaktypeOtherYesNo = "yes"

      val next = breaksInCare fillPageWith data submitPage()
      next.ctx.browser.findFirst("#startedCaring_date_day").isDisplayed should beFalse
    }

    "Break in care fill data" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCareTypePage(context) goToThePage()
      val data = new TestData
      data.BreaktypeNoneCheckbox = someTrue.get
      data.BreaktypeOtherYesNo = "yes"
      val next = breaksInCare fillPageWith data submitPage()
      val sunday = DayMonthYear(7, 6, 2015)

      next.ctx.browser.fill("#dpOtherEnded_date_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#dpOtherEnded_date_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#dpOtherEnded_date_year") `with` sunday.year.get.toString

      next.ctx.browser.click("#startedCaring_answer_yes")
      next.ctx.browser.fill("#startedCaring_date_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#startedCaring_date_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#startedCaring_date_year") `with` sunday.year.get.toString
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
          "dpOtherEnded.date.day" -> "1",
          "dpOtherEnded.date.month" -> "1",
          "dpOtherEnded.date.year" -> "2001",
          "dpOtherEnded.time" -> "10",
          "startedCaring.answer" -> Mappings.yes,
          "startedCaring.date.day" -> "2",
          "startedCaring.date.month" -> "1",
          "startedCaring.date.year" -> "2001",
          "startedCaring.time" -> "12",
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
          "dpOtherEnded.date.day" -> "1",
          "dpOtherEnded.date.month" -> "1",
          "dpOtherEnded.date.year" -> "",
          "dpOtherEnded.time" -> "10",
          "startedCaring.answer" -> Mappings.yes,
          "startedCaring.date.day" -> "2",
          "startedCaring.date.month" -> "1",
          "startedCaring.date.year" -> "2001",
          "startedCaring.time" -> "12",
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
          "dpOtherEnded.date.day" -> "1",
          "dpOtherEnded.date.month" -> "1",
          "dpOtherEnded.date.year" -> "2001",
          "dpOtherEnded.time" -> "10",
          "startedCaring.answer" -> Mappings.yes,
          "startedCaring.date.day" -> "2",
          "startedCaring.date.month" -> "1",
          "startedCaring.date.year" -> "2001",
          "startedCaring.time" -> "12",
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
          "dpOtherEnded.date.day" -> "1",
          "dpOtherEnded.date.month" -> "1",
          "dpOtherEnded.date.year" -> "2001",
          "dpOtherEnded.time" -> "10",
          "startedCaring.answer" -> Mappings.yes,
          "startedCaring.date.day" -> "2",
          "startedCaring.date.month" -> "1",
          "startedCaring.date.year" -> "2001",
          "startedCaring.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWasDp.text" -> "test1",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test")

      val result = GBreaksInCareOther.submit(request1)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
          "iterationID" -> "2",
          "dpOtherEnded.date.day" -> "1",
          "dpOtherEnded.date.month" -> "1",
          "dpOtherEnded.date.year" -> "2001",
          "dpOtherEnded.time" -> "10",
          "startedCaring.answer" -> Mappings.yes,
          "startedCaring.date.day" -> "2",
          "startedCaring.date.month" -> "1",
          "startedCaring.date.year" -> "2001",
          "startedCaring.time" -> "12",
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
          "dpOtherEnded.date.day" -> "1",
          "dpOtherEnded.date.month" -> "1",
          "dpOtherEnded.date.year" -> "2001",
          "dpOtherEnded.time" -> "10",
          "startedCaring.answer" -> Mappings.yes,
          "startedCaring.date.day" -> "2",
          "startedCaring.date.month" -> "1",
          "startedCaring.date.year" -> "2001",
          "startedCaring.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWasDp.text" -> "test1",
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test")

      val result = GBreaksInCareOther.submit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
          "iterationID" -> breakId1,
          "dpOtherEnded.date.day" -> "1",
          "dpOtherEnded.date.month" -> "1",
          "dpOtherEnded.date.year" -> yearUpdate.toString,
          "dpOtherEnded.time" -> "10",
          "startedCaring.answer" -> Mappings.yes,
          "startedCaring.date.day" -> "2",
          "startedCaring.date.month" -> "1",
          "startedCaring.date.year" -> "2001",
          "startedCaring.time" -> "12",
          "whereWasDp.answer" -> BreaksInCareOtherOptions.Holiday,
          "whereWereYou.answer" -> BreaksInCareOtherOptions.SomewhereElse,
          "whereWereYou.text" -> "test"
        )

      GBreaksInCareOther.submit(requestUpdate)

      getClaimFromCache(result).questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) => b.breaks.head.yourStayEnded.get.date.get.year.get shouldEqual yearUpdate
      }
    }
  }
  section("unit", models.domain.Breaks.id)
}
