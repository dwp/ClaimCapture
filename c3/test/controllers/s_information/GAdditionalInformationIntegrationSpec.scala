package controllers.s_information

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.BrowserMatchers
import controllers.ClaimScenarioFactory
import utils.pageobjects.PageObjects
import utils.pageobjects.s_information.GAdditionalInfoPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_consent_and_declaration.GDeclarationPage

class GAdditionalInformationIntegrationSpec extends Specification with Tags {
  "Additional information" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo(GAdditionalInfoPage.url)
      urlMustEqual(GAdditionalInfoPage.url)
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val additionalInfoPage = GAdditionalInfoPage(context)
      additionalInfoPage goToThePage()
      additionalInfoPage submitPage()

      additionalInfoPage.listErrors.size mustEqual 2
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val additionalInfoPage = GAdditionalInfoPage(context) goToThePage()
      additionalInfoPage fillPageWith ClaimScenarioFactory.s11ConsentAndDeclaration
      val previewPage = additionalInfoPage submitPage()

      previewPage must beAnInstanceOf[PreviewPage]
    }


    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  GAdditionalInfoPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 2
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  GAdditionalInfoPage(context)
      val claim = ClaimScenarioFactory.s11ConsentAndDeclaration
      page goToThePage()
      page fillPageWith claim

      val g2 = page submitPage() submitPage()

      g2 must beAnInstanceOf[GDeclarationPage]
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}