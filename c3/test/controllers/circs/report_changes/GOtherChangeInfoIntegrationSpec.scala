package controllers.circs.report_changes

import utils.WithBrowser
import utils.pageobjects.circumstances.origin.GOriginPage
import utils.pageobjects.circumstances.start_of_process.{GCircsYourDetailsPage, GReportChangesPage}
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable._
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.GOtherChangeInfoPage

class GOtherChangeInfoIntegrationSpec extends Specification {

  section("integration", models.domain.CircumstancesIdentification.id)
  "Other Change Info" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GOtherChangeInfoPage(context)
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with PageObjects{
			val page =  GOriginPage(context)
      val newPage = page goToThePage(throwException = false)

      val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
      val otherChangeInfoPage = newPage runClaimWith (claim, GOtherChangeInfoPage.url)

      otherChangeInfoPage must beAnInstanceOf[GOtherChangeInfoPage]

      val prevPage = otherChangeInfoPage.goBack()
      prevPage.url mustEqual pageBeforeFunctionsUrl
    }

    "navigate to next page" in new WithBrowser with PageObjects{
			val page =  GOtherChangeInfoPage(context)
      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
