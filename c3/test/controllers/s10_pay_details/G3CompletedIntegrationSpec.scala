package controllers.s10_pay_details

import utils.pageobjects.s10_pay_details._
import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.S11_consent_and_declaration
import controllers.BrowserMatchers

class G3CompletedIntegrationSpec extends Specification with Tags {

  "PayDetails Completion" should {
    "be presented" in new WithBrowser with G3PayDetailsCompletedPageContext {
      page goToThePage()
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      browser.goTo("/pay-details/completed")
      titleMustEqual("Additional Information - Consent and Declaration")
    }

    "contain the completed forms" in new WithBrowser with G1HowWePayYouPageContext {
      val claim = ClaimScenarioFactory.s6PayDetails()
      page goToThePage()
      page fillPageWith claim
      page submitPage()
      val completedPage = page goToPage (new G3PayDetailsCompletedPage(browser))
      completedPage must beAnInstanceOf[G3PayDetailsCompletedPage]
      completedPage.listCompletedForms.size shouldEqual 1
    }

    "navigate back to 'How we pay'" in new WithBrowser with G1HowWePayYouPageContext {
      val claim = ClaimScenarioFactory.s6PayDetails()
      page goToThePage()
      page fillPageWith claim
      val completedPage = page submitPage()
      val howWePayPage = completedPage.goBack()
      howWePayPage must beAnInstanceOf[G1HowWePayYouPage]
    }

    "navigate back to 'Bank Building Society Details'" in new WithBrowser with G3PayDetailsCompletedPageContext {

      val claim = ClaimScenarioFactory.s6BankBuildingSocietyDetails()
      val bankBuildingSocietyPage = new G2BankBuildingSocietyDetailsPage (browser)
      bankBuildingSocietyPage goToThePage()
      bankBuildingSocietyPage fillPageWith claim
      bankBuildingSocietyPage submitPage()

      val g2Page = page.goBack()
      g2Page must beAnInstanceOf[G2BankBuildingSocietyDetailsPage]
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with G3PayDetailsCompletedPageContext {
      page goToThePage()
      val consentPage = page.submitPage()
     consentPage must beAnInstanceOf[S11_consent_and_declaration.G1AdditionalInfoPage]
    }
  } section("integration", models.domain.PayDetails.id)
}