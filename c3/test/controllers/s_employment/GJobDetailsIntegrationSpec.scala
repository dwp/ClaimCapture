package controllers.s_employment

import utils.pageobjects.PageObjects
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_employment.{GBeenEmployedPage, GEmploymentPage, GLastWagePage, GJobDetailsPage}

import language.reflectiveCalls
import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}
import controllers.ClaimScenarioFactory._

class GJobDetailsIntegrationSpec extends Specification with Tags {
  "Your job" should {
    "present" in new WithBrowser with PageObjects {

      val page = GJobDetailsPage(context) goToThePage()

      page must beAnInstanceOf[GJobDetailsPage]
    }

    "accept only mandatory data" in new WithBrowser with PageObjects {
      val page = GJobDetailsPage(context) goToThePage()

      page fillPageWith s7MandatoryJobDetails()

      val lastWage = page.submitPage()

      lastWage must beAnInstanceOf[GLastWagePage]
    }

    "accept all data" in new WithBrowser with PageObjects {

      val page = GJobDetailsPage(context) goToThePage()

      page fillPageWith s7Employment()

      val lastWage = page.submitPage()

      lastWage must beAnInstanceOf[GLastWagePage]
    }

    """go back to "employment history".""" in new WithBrowser with PageObjects{
      val claimDate = new GClaimDatePage(context) goToThePage()
      claimDate.fillPageWith(s7EmployedNotSelfEmployed())
      claimDate.submitPage()

      val employment = new GEmploymentPage(claimDate.ctx) goToThePage()
      employment.fillPageWith(s7EmployedNotSelfEmployed())
      val jobDetails = employment.submitPage()

      jobDetails must beAnInstanceOf[GJobDetailsPage]
      jobDetails.goBack() must beAnInstanceOf[GEmploymentPage]
    }

    "hours a week must be visible when clicked back" in new WithBrowser with PageObjects{
      val page = GJobDetailsPage(context) goToThePage()

      page fillPageWith s7Employment()

      val lastWage = page.submitPage()

      lastWage must beAnInstanceOf[GLastWagePage]
      val yourDetails = lastWage.goBack()

      yourDetails.ctx.browser.find("#hoursPerWeek").size mustEqual 1

    }

  } section("integration", models.domain.Employed.id)

}