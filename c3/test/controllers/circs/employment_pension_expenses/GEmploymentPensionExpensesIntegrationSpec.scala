package controllers.circs.employment_pension_expenses

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes._

class GEmploymentPensionExpensesIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Report a change in your circumstance - Employment" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()
      page must beAnInstanceOf[GEmploymentChangePage]
    }

    "navigate to next page for current employment" in new WithBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText must contain("Do you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_no")
      page.ctx.browser.click("#payForThings_answer_no")
      page.ctx.browser.click("#careCosts_answer_no")
      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate to next page for past employment" in new WithBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPastJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText must contain("Did you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_no")
      page.ctx.browser.click("#payForThings_answer_no")
      page.ctx.browser.click("#careCosts_answer_no")
      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate to next page for future employment" in new WithBrowser with PageObjects {
      val page = GEmploymentPayPage.fillFutureJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText must contain("Will you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_no")
      page.ctx.browser.click("#payForThings_answer_no")
      page.ctx.browser.click("#careCosts_answer_no")
      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate back for current employment" in new WithBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      val backPage = page goBack ()
      backPage must beAnInstanceOf[GEmploymentPayPage]
    }

    "navigate back for past employment" in new WithBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPastJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      val backPage = page goBack ()
      backPage must beAnInstanceOf[GEmploymentPayPage]
    }

    "navigate back for future employment" in new WithBrowser with PageObjects {
      val page = GEmploymentPayPage.fillFutureJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]
      val backPage = page goBack ()
      backPage must beAnInstanceOf[GEmploymentPayPage]
    }

    "persist data in claim when go forward and back" in new WithBrowser with PageObjects {
      val page = GEmploymentPayPage.fillPresentJobPayDetails(context, testData => {})
      page must beAnInstanceOf[GEmploymentPensionExpensesPage]

      page.ctx.browser.find("#payIntoPension_answer_questionLabel").getText must contain("Do you pay into a pension?")
      page.ctx.browser.click("#payIntoPension_answer_yes")
      page.ctx.browser.fill("#payIntoPension_whatFor") `with` "Company pension scheme"

      page.ctx.browser.click("#payForThings_answer_yes")
      page.ctx.browser.fill("#payForThings_whatFor") `with` "Payed for work boots"

      page.ctx.browser.click("#careCosts_answer_yes")
      page.ctx.browser.fill("#careCosts_whatFor") `with` "Payed grandma for childcare"

      page.ctx.browser.fill("#moreAboutChanges") `with` "Nothing else to tell"

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
      val expensesPageAgain=nextPage goBack()
      expensesPageAgain must beAnInstanceOf[GEmploymentPensionExpensesPage]
      expensesPageAgain.ctx.browser.find("#payIntoPension_whatFor").getText mustEqual ("Company pension scheme")
      expensesPageAgain.ctx.browser.find("#payForThings_whatFor").getText mustEqual ("Payed for work boots")
      expensesPageAgain.ctx.browser.find("#careCosts_whatFor").getText mustEqual ("Payed grandma for childcare")
      expensesPageAgain.ctx.browser.find("#moreAboutChanges").getText mustEqual ("Nothing else to tell")
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
