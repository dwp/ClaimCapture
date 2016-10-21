package controllers.circs.employment_pension_expenses

import controllers.circs.report_changes.GEmploymentPensionExpenses
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes._
import utils.{WithApplication, WithJsBrowser}

class GEmploymentPensionExpensesContentSpec extends Specification {
  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Circs Employment Pension and Expenses page " should {
    "present page correctly" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
      val result = GEmploymentPensionExpenses.present(request)
      status(result) mustEqual OK
    }

    "contain correct labels for present ongoing employment" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]

      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual("Do you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.find("#payIntoPension_whatFor_questionLabel").getText mustEqual("Give details of each pension you pay into, including how much and how often you pay.")

      page.ctx.browser.find("#payForThings_answer_questionLabel").getText mustEqual("Do you pay for things you need to do your job?")
      page.ctx.browser.find("#payForThings_answer_questionLabel +.form-hint").getText mustEqual("This means anything you have to pay for yourself - not expenses your company pays.")
      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.find("#payForThings_whatFor_questionLabel").getText mustEqual("Give details of what you need to buy, why you need it and how much it costs.")

      page.ctx.browser.find("#careCosts_answer_questionLabel").getText mustEqual("Do you have any care costs because of this work?")
      page.ctx.browser.find("#careCosts_answer_questionLabel +.form-hint").getText mustEqual("This includes childcare costs as well as costs for looking after the person you care for.")
      page.ctx.browser.click("#careCosts_answer_yes")
      page.ctx.browser.find("#careCosts_whatFor_questionLabel").getText mustEqual("Give details of who you pay and what it costs.")
      page.ctx.browser.find("#careCosts_whatFor_questionLabel +.form-hint").getText mustEqual("If you pay a family member let us know their relationship to you and the person you care for.")
    }

    "contain correct labels for past employment" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPastJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]

      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual("Did you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.find("#payIntoPension_whatFor_questionLabel").getText mustEqual("Give details of each pension you paid into, including how much and how often you paid into them.")

      page.ctx.browser.find("#payForThings_answer_questionLabel").getText mustEqual("Did you pay for things you needed to do your job?")
      page.ctx.browser.find("#payForThings_answer_questionLabel +.form-hint").getText mustEqual("This means anything you had to pay for yourself - not expenses your company paid for.")
      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.find("#payForThings_whatFor_questionLabel").getText mustEqual("Give details of what you needed to buy, why you needed it and how much it cost.")

      page.ctx.browser.find("#careCosts_answer_questionLabel").getText mustEqual("Did you have any care costs because of this work?")
      page.ctx.browser.find("#careCosts_answer_questionLabel +.form-hint").getText mustEqual("This includes childcare costs as well as costs for looking after the person you care for.")
      page.ctx.browser.click("#careCosts_answer_yes")
      page.ctx.browser.find("#careCosts_whatFor_questionLabel").getText mustEqual("Give details of who you paid and what it cost.")
      page.ctx.browser.find("#careCosts_whatFor_questionLabel +.form-hint").getText mustEqual("If you paid a family member let us know their relationship to you and the person you care for.")
    }

    "contain correct labels for future employment" in new WithJsBrowser with PageObjects{
      val page = GEmploymentPayPage.fillFutureJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]

      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual("Will you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.find("#payIntoPension_whatFor_questionLabel").getText mustEqual("Give details of each pension you will pay into, including how much and how often you will pay.")

      page.ctx.browser.find("#payForThings_answer_questionLabel").getText mustEqual("Will you pay for things you need to do your job?")
      page.ctx.browser.find("#payForThings_answer_questionLabel +.form-hint").getText mustEqual("This means anything you will have to pay for yourself - not expenses your company will pay.")
      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.find("#payForThings_whatFor_questionLabel").getText mustEqual("Give details of what you will need to buy, why you will need it and how much it will cost.")

      page.ctx.browser.find("#careCosts_answer_questionLabel").getText mustEqual("Will you have any care costs because of this work?")
      page.ctx.browser.find("#careCosts_answer_questionLabel +.form-hint").getText mustEqual("This includes childcare costs as well as costs for looking after the person you care for.")
      page.ctx.browser.click("#careCosts_answer_yes")
      page.ctx.browser.find("#careCosts_whatFor_questionLabel").getText mustEqual("Give details of who you will pay and what it will cost.")
      page.ctx.browser.find("#careCosts_whatFor_questionLabel +.form-hint").getText mustEqual("If you will pay a family member let us know their relationship to you and the person you care for.")
    }

    "set the text area field lengths correctly .. all variants default to using present ongoing employment xsd" in new WithJsBrowser with PageObjects{
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_whatFor").getAttribute("maxlength") mustEqual("300")
      page.ctx.browser.find("#payForThings_whatFor").getAttribute("maxlength") mustEqual("300")
      page.ctx.browser.find("#careCosts_whatFor").getAttribute("maxlength") mustEqual("300")
      page.ctx.browser.find("#moreAboutChanges").getAttribute("maxlength") mustEqual("3000")
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
