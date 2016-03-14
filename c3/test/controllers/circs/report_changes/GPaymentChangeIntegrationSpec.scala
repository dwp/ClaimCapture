package controllers.circs.report_changes

import utils.WithBrowser
import utils.pageobjects.circumstances.start_of_process.{GCircsYourDetailsPage, GReportChangesPage}
import utils.pageobjects.circumstances.report_changes._
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable._
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage

class GPaymentChangeIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Report a change in your circumstances" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GPaymentChangePage(context)
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with PageObjects {
      val page = GReportChangesPage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.paymentChangesChangeInfo
      page fillPageWith (claim)
      val completedPage = page submitPage()

      val reportChangesPage = completedPage runClaimWith(claim, GPaymentChangePage.url)

      reportChangesPage must beAnInstanceOf[GPaymentChangePage]

      val prevPage = reportChangesPage.goBack()
      println("Previous page " + prevPage.url)
      prevPage.url mustEqual (pageBeforeFunctionsUrl)
    }

    "navigate to next page when 'yes' is selected for currently paid into bank selected" in new WithBrowser with PageObjects {
      val page = GPaymentChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesPaymentChangeScenario1
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate to next page when 'no' is selected for currently paid into bank selected" in new WithBrowser with PageObjects {
      val page = GPaymentChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesPaymentChangeScenario2
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
