package controllers.s_breaks

import app.CircsBreaksWhereabouts._
import models.DayMonthYear
import models.domain.{BreaksInCare, Claiming}
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.s_breaks.GBreaksInCarePage
import utils.pageobjects.{PageObjects, TestData}
import utils.{WithApplication, WithJsBrowser}

class GBreakSpec extends Specification {
  "Break" should {
    val breakId1 = "1"

    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GBreak.present("")(request)
      status(result) mustEqual OK
    }

    "Break in care start time and end time by default should not be displayed" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"

      val next = breaksInCare fillPageWith data submitPage()
      next.ctx.browser.findFirst("#startTime").isDisplayed should beFalse
    }

    "Break in care end date by default should not be displayed" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"
      val next = breaksInCare fillPageWith data submitPage()
      next.ctx.browser.findFirst("#hasBreakEnded_date_day").isDisplayed should beFalse
      next.ctx.browser.findFirst("#hasBreakEnded_date_month").isDisplayed should beFalse
      next.ctx.browser.findFirst("#hasBreakEnded_date_year").isDisplayed should beFalse
    }

    "Break in care start/end time should only be displayed if start/end date is Monday or Friday" in new WithJsBrowser with PageObjects {
      val breaksInCare = GBreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"
      val next = breaksInCare fillPageWith data submitPage()

      val sunday = DayMonthYear(7, 6, 2015)

      next.ctx.browser.fill("#start_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#start_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#start_year") `with` sunday.year.get.toString
      next.ctx.browser.findFirst("#startTime").isDisplayed should beFalse

      next.ctx.browser.click("#hasBreakEnded_answer_yes")
      next.ctx.browser.fill("#hasBreakEnded_date_day") `with` sunday.day.get.toString
      next.ctx.browser.fill("#hasBreakEnded_date_month") `with` sunday.month.get.toString
      next.ctx.browser.fill("#hasBreakEnded_date_year") `with` sunday.year.get.toString
      next.ctx.browser.findFirst("#endTime").isDisplayed should beFalse
    }

    "reject when submitted with missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GBreak.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> Holiday,
        "wherePerson.answer" -> Holiday,
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      val result = GBreak.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "reject when submitted with other selected but missing other data" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> SomewhereElse,
        "whereYou.text" -> "",
        "wherePerson.answer" -> SomewhereElse,
        "wherePerson.text" -> "",
        "medicalDuringBreak" -> "no")

      val result = GBreak.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission with other selected" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> SomewhereElse,
        "whereYou.text" -> "Outer space",
        "wherePerson.answer" -> SomewhereElse,
        "wherePerson.text" -> "Underwater",
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      val result = GBreak.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add 2 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> Holiday,
        "wherePerson.answer" -> Holiday,
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      val result = GBreak.submit(request1)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
        "iterationID" -> "2",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> Holiday,
        "wherePerson.answer" -> Holiday,
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      GBreak.submit(request2)

      val claim = getClaimFromCache(result)

      claim.questionGroup(BreaksInCare) must beLike { case Some(b: BreaksInCare) => b.breaks.size mustEqual 2 }
    }

    "update existing break" in new WithApplication with Claiming {
      val requestNew = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> Holiday,
        "wherePerson.answer" -> Holiday,
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      val result = GBreak.submit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
        "iterationID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> yearUpdate.toString,
        "whereYou.answer" -> Holiday,
        "wherePerson.answer" -> Holiday,
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      GBreak.submit(requestUpdate)

      getClaimFromCache(result).questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) =>
          b.breaks.head.start.year.get shouldEqual yearUpdate
      }
    }
  }
  section("unit", models.domain.CareYouProvide.id)
}
