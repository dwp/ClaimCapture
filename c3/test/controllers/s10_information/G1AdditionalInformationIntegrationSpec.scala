package controllers.s10_information

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import controllers.ClaimScenarioFactory
import utils.pageobjects.PageObjects
import utils.pageobjects.s10_information.G1AdditionalInfoPage
import utils.pageobjects.s11_pay_details.G2BankBuildingSocietyDetailsPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s12_consent_and_declaration.G3DeclarationPage

class G1AdditionalInformationIntegrationSpec extends Specification with Tags {
  "Additional information" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo(G1AdditionalInfoPage.url)
      urlMustEqual(G1AdditionalInfoPage.url)
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val additionalInfoPage = G1AdditionalInfoPage(context)
      additionalInfoPage goToThePage()
      additionalInfoPage submitPage()

      additionalInfoPage.listErrors.size mustEqual 2
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val additionalInfoPage = G1AdditionalInfoPage(context) goToThePage()
      additionalInfoPage fillPageWith ClaimScenarioFactory.s11ConsentAndDeclaration
      val previewPage = additionalInfoPage submitPage()

      previewPage must beAnInstanceOf[PreviewPage]
    }

    "navigate back to Bank/Building society details - How we pay you" in new WithBrowser with PageObjects{
			val page =  G2BankBuildingSocietyDetailsPage(context)
      val claim = ClaimScenarioFactory.s6BankBuildingSocietyDetails()
      page goToThePage()
      page fillPageWith claim
      val consentDeclarationPage = page submitPage()
      
      val bankBuildingSocietyPage = consentDeclarationPage.goBack()

      bankBuildingSocietyPage must beAnInstanceOf[G2BankBuildingSocietyDetailsPage]
    }
    
    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  G1AdditionalInfoPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 2
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G1AdditionalInfoPage(context)
      val claim = ClaimScenarioFactory.s11ConsentAndDeclaration
      page goToThePage()
      page fillPageWith claim

      val g2 = page submitPage() submitPage()
      
      g2 must beAnInstanceOf[G3DeclarationPage]
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}