package controllers.circs.s2_report_changes

import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.G6AddressChangePage
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import org.specs2.mutable.{Tags, Specification}

/**
 * Created by neddakaltcheva on 2/14/14.
 */
class G6AddressChangeIntegrationSpec  extends Specification with Tags {
  "Address change" should {

    "be presented" in new WithBrowser with PageObjects{
      val page =  G6AddressChangePage(context)
      page goToThePage()
    }

    "navigate to next page when 'no' is selected for still caring" in new WithBrowser with PageObjects{
      val page =  G6AddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeNo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

  } section("integration", models.domain.CircumstancesIdentification.id)

}
