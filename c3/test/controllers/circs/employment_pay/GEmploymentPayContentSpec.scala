package controllers.circs.employment_pay

import controllers.circs.report_changes.{GEmploymentPensionExpenses}
import org.specs2.mutable._
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes._
import utils.{WithJsBrowser}

class GEmploymentPayContentSpec extends Specification {
  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Circs Employment Pay page " should {
    "contain correct labels for present ongoing employment when been-paid=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#pastpresentfuture").getValue mustEqual ("present")
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Have you been paid yet?")
      page.ctx.browser.click("#paid_yes")

      page.ctx.browser.find("#howmuch_questionLabel").getText mustEqual ("How much were you paid before tax or other deductions?")
      page.ctx.browser.find("#paydate_questionLabel").getText mustEqual ("What date did you receive your last pay?")
      page.ctx.browser.find("#howOften").getText mustEqual ("How often are you paid?")
    }

    "contain correct labels for present ongoing employment when been-paid=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#pastpresentfuture").getValue mustEqual ("present")
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Have you been paid yet?")
      page.ctx.browser.click("#paid_no")

      page.ctx.browser.find("#howmuch_questionLabel").getText mustEqual ("How much do you expect to be paid before tax or other deductions?")
      page.ctx.browser.find("#paydate_questionLabel").getText mustEqual ("What date do you expect to be paid?")
      page.ctx.browser.find("#howOften").getText mustEqual ("How often do you expect to be paid?")
    }

    "contain correct labels for past employment when been-paid=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#pastpresentfuture").getValue mustEqual ("past")
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Have you received your last pay?")
      page.ctx.browser.click("#paid_yes")

      page.ctx.browser.find("#howmuch_questionLabel").getText mustEqual ("How much were you paid before tax or other deductions?")
      page.ctx.browser.find("#paydate_questionLabel").getText mustEqual ("What date did you receive your last pay?")
      page.ctx.browser.find("#whatWasIncluded_questionLabel").getText must contain("What was included in this pay? (optional)")
      page.ctx.browser.find("#howOften").getText mustEqual ("How often were you paid?")
      page.ctx.browser.find("#owedMoney_questionLabel").getText mustEqual ("Does your employer owe you any money?")
      page.ctx.browser.click("#owedMoney_yes")
      page.ctx.browser.find("#owedMoneyInfo_questionLabel").getText mustEqual ("Tell us what this is for and how much")
    }

    "contain correct labels for past ongoing employment when been-paid=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#pastpresentfuture").getValue mustEqual ("past")
      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Have you received your last pay?")
      page.ctx.browser.click("#paid_no")

      page.ctx.browser.find("#howmuch_questionLabel").getText mustEqual ("How much do you expect to be paid before tax or other deductions?")
      page.ctx.browser.find("#paydate_questionLabel").getText mustEqual ("What date do you expect to be paid?")
      page.ctx.browser.find("#whatWasIncluded_questionLabel").getText must contain("What do you expect to be included in this pay? (optional)")
      page.ctx.browser.find("#howOften").getText mustEqual ("How often were you paid?")
      page.ctx.browser.find("#owedMoney_questionLabel").getText mustEqual ("Does your employer owe you any money?")
      page.ctx.browser.click("#owedMoney_yes")
      page.ctx.browser.find("#owedMoneyInfo_questionLabel").getText mustEqual ("Tell us what this is for and how much")
    }

    "contain correct labels for future employment when know-how-pay=YES" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillFutureJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#pastpresentfuture").getValue mustEqual ("future")

      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Do you know how much you will be paid?")
      page.ctx.browser.click("#paid_yes")
      println(page.source)
      page.ctx.browser.find("#howmuch_questionLabel").getText mustEqual ("How much will you be paid before tax or other deductions?")
      page.ctx.browser.find("#paydate_questionLabel").getText mustEqual ("What date do you expect to receive your first pay?")
      page.ctx.browser.find("#howOften").getText mustEqual ("How often will you be paid?")
    }

    "contain correct labels for future employment when know-how-pay=NO" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage.fillFutureJobDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPayPage]
      page.ctx.browser.find("#pastpresentfuture").getValue mustEqual ("future")

      page.ctx.browser.find("#paid_questionLabel").getText mustEqual ("Do you know how much you will be paid?")
      page.ctx.browser.click("#paid_no")
      println(page.source)
      page.ctx.browser.find("#howmuch_questionLabel").getText mustEqual ("")
      page.ctx.browser.find("#paydate_questionLabel").getText mustEqual ("")
      page.ctx.browser.find("#howOften").getText mustEqual ("")
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
