package controllers.circs.employment_pay

import controllers.circs.report_changes.{GEmploymentPay, GEmploymentPensionExpenses}
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes._
import utils.{WithApplication, WithJsBrowser}

class GEmploymentPayContentSpec extends Specification {
  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Circs Employment Pay page " should {
    "present page correctly" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
      val result = GEmploymentPay.present(request)
      status(result) mustEqual OK
    }

    "contain correct labels for present ongoing employment when been-paid=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
      page must beAnInstanceOf[GStartedEmploymentAndOngoingPage]

      page.ctx.browser.find("#beenPaidYet_questionLabel").getText mustEqual ("Have you been paid yet?")
      page.ctx.browser.click("#beenPaidYet_yes")
      page.ctx.browser.find("#howMuchPaid_questionLabel").getText mustEqual ("How much were you paid before tax or other deductions?")
      page.ctx.browser.find("#whatDatePaid_questionLabel").getText mustEqual ("What date did you receive your last pay?")
      page.ctx.browser.find("#howOften").getText mustEqual ("How often are you paid?")
    }

    "contain correct labels for present ongoing employment when been-paid=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
      page must beAnInstanceOf[GStartedEmploymentAndOngoingPage]

      page.ctx.browser.find("#beenPaidYet_questionLabel").getText mustEqual ("Have you been paid yet?")
      page.ctx.browser.click("#beenPaidYet_no")
      page.ctx.browser.find("#howMuchPaid_questionLabel").getText mustEqual ("How much do you expect to be paid before tax or other deductions?")
      page.ctx.browser.find("#whatDatePaid_questionLabel").getText mustEqual ("What date do you expect to be paid?")
      page.ctx.browser.find("#howOften").getText mustEqual ("How often do you expect to be paid?")
    }

    "contain correct labels for past employment when been-paid=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
      page must beAnInstanceOf[GStartedAndFinishedEmploymentPage]

      page.ctx.browser.find("#beenPaidYet_questionLabel").getText mustEqual ("Have you received your last pay?")
      page.ctx.browser.click("#beenPaidYet_yes")
      println(page.source)
      page.ctx.browser.find("#howMuchPaid_questionLabel").getText mustEqual ("How much were you paid before tax or other deductions?")
      page.ctx.browser.find("#dateLastPaid_questionLabel").getText mustEqual ("What date did you receive your last pay?")
      page.ctx.browser.find("#whatWasIncluded_questionLabel").getText must contain("What was included in this pay?(optional)")
      page.ctx.browser.find("#howOften_frequency_questionLabel").getText mustEqual ("How often were you paid?")
      page.ctx.browser.find("#employerOwesYouMoney_questionLabel").getText mustEqual ("Does your employer owe you any money?")
      page.ctx.browser.click("#employerOwesYouMoney_yes")
      page.ctx.browser.find("#employerOwesYouMoneyInfo_questionLabel").getText mustEqual ("Tell us what this is for and how much")
    }

    "contain correct labels for past ongoing employment when been-paid=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
      page must beAnInstanceOf[GStartedAndFinishedEmploymentPage]
      page.ctx.browser.find("#beenPaidYet_questionLabel").getText mustEqual ("Have you received your last pay?")
      page.ctx.browser.click("#beenPaidYet_no")
      println(page.source)
      page.ctx.browser.find("#howMuchPaid_questionLabel").getText mustEqual ("How much do you expect to be paid before tax or other deductions?")
      page.ctx.browser.find("#dateLastPaid_questionLabel").getText mustEqual ("What date do you expect to be paid?")
      page.ctx.browser.find("#whatWasIncluded_questionLabel").getText must contain("What do you expect to be included in this pay?(optional)")
      page.ctx.browser.find("#howOften_frequency_questionLabel").getText mustEqual ("How often were you paid?")
      page.ctx.browser.find("#employerOwesYouMoney_questionLabel").getText mustEqual ("Does your employer owe you any money?")
      page.ctx.browser.click("#employerOwesYouMoney_yes")
      page.ctx.browser.find("#employerOwesYouMoneyInfo_questionLabel").getText mustEqual ("Tell us what this is for and how much")
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
