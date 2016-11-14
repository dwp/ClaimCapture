package controllers.circs.address_change

import controllers.CircumstancesScenarioFactory
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.GAddressChangePage

class GAddressChangeIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)

  "Address change" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      page goToThePage()
    }

    "navigate to next page when 'yes' is selected for still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate to next page when 'no' is selected for still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeNo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
    }

    "navigate to next page and back when postcode has spaces" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangePreviousPostcode = " PR11  4JQ "
      claim.CircumstancesAddressChangeNewPostcode = " PR12  4JQ "
      claim.CircumstancesAddressChangeSameAddressTheirPostcode = " PR13  4JQ "
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage.url mustEqual pageAfterFunctionsUrl
      val addressChangePageAgain = nextPage goBack()
      addressChangePageAgain.source must contain("PR11 4JQ")
      addressChangePageAgain.source must contain("PR12 4JQ")
      addressChangePageAgain.source must contain("PR13 4JQ")
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
