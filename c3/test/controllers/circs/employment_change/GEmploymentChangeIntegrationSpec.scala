package controllers.circs.employment_change

import controllers.CircumstancesScenarioFactory
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.origin.GOriginPage
import utils.pageobjects.circumstances.report_changes.GEmploymentChangePage

class GEmploymentChangeIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Report a change in your circumstance - Employment" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()
    }

    "navigate to the previous page" in new WithBrowser with PageObjects {
      val page = GOriginPage(context)
      val newPage = page goToThePage(throwException = false)

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentNotStartedYet
      newPage fillPageWith(claim)
      val completedPage = newPage submitPage()
      val employmentChangePage = completedPage runClaimWith(claim, GEmploymentChangePage.url)
      employmentChangePage must beAnInstanceOf[GEmploymentChangePage]
      val prevPage = employmentChangePage.goBack()
      prevPage.url mustEqual pageBeforeFunctionsUrl
    }

    "navigate to next page when not caring and not yet started self-employment details added" in new WithBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentNotStartedYet
      page fillPageWith(claim)

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate to next page when caring and self-employment started details added" in new WithBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentStartedAndOngoing
      page fillPageWith(claim)

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate to next page and back for postcode with space" in new WithBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentNotStartedYet
      claim.CircumstancesEmploymentChangeEmployerPostcode = " PR1  1HB "
      page fillPageWith(claim)

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
      val prevPage = nextPage.goBack()
      prevPage.source must contain("PR1 1HB")
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
