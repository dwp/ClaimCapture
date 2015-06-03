package controllers.s7_employment

import utils.pageobjects.PageObjects
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s7_employment.{G2BeenEmployedPage, G1EmploymentPage, G5LastWagePage, G3JobDetailsPage}

import language.reflectiveCalls
import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}
import controllers.ClaimScenarioFactory._

class G3JobDetailsIntegrationSpec extends Specification with Tags {
  "Your job" should {
    "present" in new WithBrowser with PageObjects {

      val page = G3JobDetailsPage(context) goToThePage()

      page must beAnInstanceOf[G3JobDetailsPage]
    }

    "accept only mandatory data" in new WithBrowser with PageObjects {
      val page = G3JobDetailsPage(context) goToThePage()

      page fillPageWith s7MandatoryJobDetails()

      val lastWage = page.submitPage()

      lastWage must beAnInstanceOf[G5LastWagePage]
    }

    "accept all data" in new WithBrowser with PageObjects {

      val page = G3JobDetailsPage(context) goToThePage()

      page fillPageWith s7Employment()

      val lastWage = page.submitPage()

      lastWage must beAnInstanceOf[G5LastWagePage]
    }

    """go back to "employment history".""" in new WithBrowser with PageObjects{
      val claimDate = new G1ClaimDatePage(context) goToThePage()
      claimDate.fillPageWith(s7EmployedNotSelfEmployed())
      claimDate.submitPage()

      val employment = new G1EmploymentPage(claimDate.ctx) goToThePage()
      employment.fillPageWith(s7EmployedNotSelfEmployed())
      val jobDetails = employment.submitPage()

      jobDetails must beAnInstanceOf[G3JobDetailsPage]
      jobDetails.goBack() must beAnInstanceOf[G1EmploymentPage]
    }

    "hours a week must be visible when clicked back" in new WithBrowser with PageObjects{
      val page = G3JobDetailsPage(context) goToThePage()

      page fillPageWith s7Employment()

      val lastWage = page.submitPage()

      lastWage must beAnInstanceOf[G5LastWagePage]
      val yourDetails = lastWage.goBack()

      yourDetails.ctx.browser.find("#hoursPerWeek").size mustEqual 1

    }

  } section("integration", models.domain.Employed.id)

}