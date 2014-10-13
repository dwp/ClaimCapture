package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s10_information.G1AdditionalInfoPage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s12_consent_and_declaration.G2DisclaimerPage


class PreviewIntegrationSpec extends Specification with Tags {
  "Preview" should{
    "be presented" in new WithBrowser with PageObjects{
      val page =  PreviewPage(context)
      page goToThePage()
    }

    "navigate back to Additional Info page" in new WithBrowser with PageObjects{
      val additionalInfoPage = G1AdditionalInfoPage(context)
      val additionalInfoData = ClaimScenarioFactory.s11ConsentAndDeclaration
      additionalInfoPage goToThePage ()
      additionalInfoPage fillPageWith additionalInfoData
      val previewPage = additionalInfoPage submitPage()
      previewPage must beAnInstanceOf[PreviewPage]
      previewPage goBack() must beAnInstanceOf[G1AdditionalInfoPage]
    }

    "navigate to Disclaimer page" in new WithBrowser with PageObjects {
       val previewPage = PreviewPage(context)
       previewPage goToThePage()
       val disclaimerPage = previewPage submitPage()
       disclaimerPage must beAnInstanceOf[G2DisclaimerPage]
    }

  }
}
