package controllers.circs.employment_pay

import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes._

class GEmploymentPayErrorSpec extends Specification {
  section("unit", models.domain.CircumstancesSelfEmployment.id)

  val badChars = "Remove any characters apart from numbers, letters or basic punctuation, eg commas and full stops"
  val completeSection = "You must complete this section"

  "Circs Employment Pay page " should {
    "present single error for present ongoing employment when no selections" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual (s"Have you been paid yet? - $completeSection")
    }

    "present 3 errors for present ongoing employment when paid-yet=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Have you been paid yet?")
      page.ctx.browser.click("#paid_yes")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"How much were you paid before tax or other deductions? - $completeSection")
      errors(1) mustEqual (s"What date did you receive your last pay? - $completeSection")
      errors(2) mustEqual (s"How often are you paid? - $completeSection")
    }

    "present 3 errors for present ongoing employment when paid-yet=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.click("#paid_no")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"How much do you expect to be paid before tax or other deductions? - $completeSection")
      errors(1) mustEqual (s"What date do you expect to be paid? - $completeSection")
      errors(2) mustEqual (s"How often do you expect to be paid? - $completeSection")
    }

    "present 3 errors for past employment when no selections" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      val errors = page.submitPage().listErrors
      errors.size mustEqual 2
      errors(0) mustEqual (s"Have you received your last pay? - $completeSection")
      errors(1) mustEqual (s"Does your employer owe you any money? - $completeSection")
    }

    "present 4 errors for past employment when paid-yet=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Have you received your last pay?")
      page.ctx.browser.click("#paid_yes")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 4
      errors(0) mustEqual (s"How much were you paid before tax or other deductions? - $completeSection")
      errors(1) mustEqual (s"What date did you receive your last pay? - $completeSection")
      errors(2) mustEqual (s"How often were you paid? - $completeSection")
      errors(3) mustEqual (s"Does your employer owe you any money? - $completeSection")
    }

    "present 4 errors for past employment when paid-yet=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Have you received your last pay?")
      page.ctx.browser.click("#paid_no")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 4
      errors(0) mustEqual (s"How much do you expect to be paid before tax or other deductions? - $completeSection")
      errors(1) mustEqual (s"What date do you expect to be paid? - $completeSection")
      errors(2) mustEqual (s"How often were you paid? - $completeSection")
      errors(3) mustEqual (s"Does your employer owe you any money? - $completeSection")
    }

    "present single error for future employment when no selections" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillFutureJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual (s"Do you know how much you will be paid? - $completeSection")
    }

    "present 3 errors for future employment when paid-yet=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillFutureJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Do you know how much you will be paid?")
      page.ctx.browser.click("#paid_yes")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual (s"How much will you be paid before tax or other deductions? - $completeSection")
      errors(1) mustEqual (s"What date do you expect to receive your first pay? - $completeSection")
      errors(2) mustEqual (s"How often will you be paid? - $completeSection")
    }

    "allow submit for future employment when paid-yet=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillFutureJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Do you know how much you will be paid?")
      page.ctx.browser.click("#paid_no")
      val newPage = page.submitPage()
      newPage must beAnInstanceOf[GEmploymentPensionExpensesPage]
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
