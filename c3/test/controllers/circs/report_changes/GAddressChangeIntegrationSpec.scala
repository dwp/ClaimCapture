package controllers.circs.report_changes

import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.GAddressChangePage
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import org.specs2.mutable._

/**
 * Created by neddakaltcheva on 2/14/14.
 */
class GAddressChangeIntegrationSpec  extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Address change" should {
    "be presented" in new WithBrowser with PageObjects{
      val page =  GAddressChangePage(context)
      page goToThePage()
    }

    "navigate to next page when 'yes' is selected for still caring" in new WithBrowser with PageObjects {
      val page =  GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }

    "navigate to next page when 'no' is selected for still caring" in new WithBrowser with PageObjects {
      val page =  GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeNo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }

    "show 3 errors on submitting without any mandatory data" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.addressChange
      page goToThePage()
      page fillPageWith claim

      page submitPage ()
      page.listErrors.size must beEqualTo(3)
    }

    "show error on submitting without mandatory 'person has changed address' when still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingPersonChangedAddress
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }

    "show error on submitting without mandatory 'date stopped caring' when not still caring" in new WithBrowser with PageObjects{
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingDateStoppedCaring
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }

    "show error on submitting without mandatory 'new address' when not still caring" in new WithBrowser with PageObjects{
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingNewAddress
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }

    "show errors on submitting without mandatory 'new address' and 'date stopped caring' when not still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingNewAddressAndDate

      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(2)
    }

    "show error on submitting without mandatory 'same address' when person has changed address" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressMissingSameAddress
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
