package controllers.circs.s2_report_changes

import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_start_of_process.{G2ReportAChangeInYourCircumstancesPage, G1ReportChangesPage}
import utils.pageobjects.circumstances.s2_report_changes._
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.G4OtherChangeInfoPage

class G4OtherChangeInfoIntegrationSpec extends Specification with Tags {

  "Other Change Info" should {

    "be presented" in new WithBrowser with PageObjects{
			val page =  G4OtherChangeInfoPage(context)
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with PageObjects{
			val page =  G1ReportChangesPage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
      val otherChangeInfoPage = page runClaimWith (claim, G4OtherChangeInfoPage.url)

      otherChangeInfoPage must beAnInstanceOf[G4OtherChangeInfoPage]

      val prevPage = otherChangeInfoPage.goBack()

      prevPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
    }

    "navigate to next page" in new WithBrowser with PageObjects{
			val page =  G4OtherChangeInfoPage(context)
      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

  } section("integration", models.domain.CircumstancesIdentification.id)

}
