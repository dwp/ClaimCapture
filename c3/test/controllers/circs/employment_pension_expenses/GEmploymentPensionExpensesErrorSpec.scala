package controllers.circs.employment_pension_expenses

import org.specs2.mutable._
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes._
import utils.{WithJsBrowser}

class GEmploymentPensionExpensesErrorSpec extends Specification {
  section("unit", models.domain.CircumstancesSelfEmployment.id)
  val badChars = "Remove any characters apart from numbers, letters or basic punctuation, eg commas and full stops"
  val completeSection = "You must complete this section"

  "Circs Employment Pension and Expenses page " should {
    "present - have 3 correct error messages for present/ongoing employment when no yes/no buttons selected" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Do you pay into a pension?")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"Do you pay into a pension? - $completeSection")
      errors(1) mustEqual (s"Do you pay for things you need to do your job? - $completeSection")
      errors(2) mustEqual (s"Do you have any care costs because of this work? - $completeSection")
    }

    "present - have 3 correct error messages for present/ongoing employment when no yes buttons selected but no text entered" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Do you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.click("#careCosts_answer_yes")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"Give details of each pension you pay into, including how much and how often you pay. - $completeSection")
      errors(1) mustEqual (s"Give details of what you need to buy, why you need it and how much it costs. - $completeSection")
      errors(2) mustEqual (s"Give details of who you pay and what it costs. - $completeSection")
    }

    "present - have 3 correct error messages for present/ongoing employment when no yes buttons selected and bad text entered" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Do you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.fill("#payIntoPension_whatFor") `with` "Payed $20 when dollar is bad character"

      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.fill("#payForThings_whatFor") `with` "Payed $20 when dollar is bad character"

      page.ctx.browser.click("#careCosts_answer_yes")
      page.ctx.browser.fill("#careCosts_whatFor") `with` "Payed $20 when dollar is bad character"
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      val badChars = "Remove any characters apart from numbers, letters or basic punctuation, eg commas and full stops"
      errors(0) mustEqual (s"Give details of each pension you pay into, including how much and how often you pay. - $badChars")
      errors(1) mustEqual (s"Give details of what you need to buy, why you need it and how much it costs. - $badChars")
      errors(2) mustEqual (s"Give details of who you pay and what it costs. - $badChars")
    }

    "past - have 3 correct error messages for past employment when no yes/no buttons selected" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPastJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Did you pay into a pension?")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"Did you pay into a pension? - $completeSection")
      errors(1) mustEqual (s"Did you pay for things you needed to do your job? - $completeSection")
      errors(2) mustEqual (s"Did you have any care costs because of this work? - $completeSection")
    }

    "past - have 3 correct error messages for past employment when no yes buttons selected but no text entered" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPastJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Did you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.click("#careCosts_answer_yes")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"Give details of each pension you paid into, including how much and how often you paid into them. - $completeSection")
      errors(1) mustEqual (s"Give details of what you needed to buy, why you needed it and how much it cost. - $completeSection")
      errors(2) mustEqual (s"Give details of who you paid and what it cost. - $completeSection")
    }

    "past - have 3 correct error messages for past employment when no yes buttons selected and bad text entered" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPastJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Did you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.fill("#payIntoPension_whatFor") `with` "Payed $20 when dollar is bad character"

      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.fill("#payForThings_whatFor") `with` "Payed $20 when dollar is bad character"

      page.ctx.browser.click("#careCosts_answer_yes")
      page.ctx.browser.fill("#careCosts_whatFor") `with` "Payed $20 when dollar is bad character"
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"Give details of each pension you paid into, including how much and how often you paid into them. - $badChars")
      errors(1) mustEqual (s"Give details of what you needed to buy, why you needed it and how much it cost. - $badChars")
      errors(2) mustEqual (s"Give details of who you paid and what it cost. - $badChars")
    }

    "future - have 3 correct error messages for future employment when no yes/no buttons selected" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillFutureJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Will you pay into a pension?")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"Will you pay into a pension? - $completeSection")
      errors(1) mustEqual (s"Will you pay for things you need to do your job? - $completeSection")
      errors(2) mustEqual (s"Will you have any care costs because of this work? - $completeSection")
    }

    "future - have 3 correct error messages for future employment when no yes buttons selected but no text entered" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillFutureJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Will you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.click("#careCosts_answer_yes")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"Give details of each pension you will pay into, including how much and how often you will pay. - $completeSection")
      errors(1) mustEqual (s"Give details of what you will need to buy, why you will need it and how much it will cost. - $completeSection")
      errors(2) mustEqual (s"Give details of who you will pay and what it will cost. - $completeSection")
    }

    "future - have 3 correct error messages for future employment when no yes buttons selected and bad text entered" in new WithJsBrowser with PageObjects {
      val page = GEmploymentPayPage.fillFutureJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText mustEqual ("Will you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.fill("#payIntoPension_whatFor") `with` "Payed $20 when dollar is bad character"

      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.fill("#payForThings_whatFor") `with` "Payed $20 when dollar is bad character"

      page.ctx.browser.click("#careCosts_answer_yes")
      page.ctx.browser.fill("#careCosts_whatFor") `with` "Payed $20 when dollar is bad character"
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      val badChars = "Remove any characters apart from numbers, letters or basic punctuation, eg commas and full stops"
      errors(0) mustEqual (s"Give details of each pension you will pay into, including how much and how often you will pay. - $badChars")
      errors(1) mustEqual (s"Give details of what you will need to buy, why you will need it and how much it will cost. - $badChars")
      errors(2) mustEqual (s"Give details of who you will pay and what it will cost. - $badChars")
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
