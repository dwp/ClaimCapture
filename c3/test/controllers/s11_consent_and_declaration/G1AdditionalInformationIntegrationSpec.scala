package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate
import controllers.BrowserMatchers
import utils.pageobjects.S11_consent_and_declaration._
import utils.pageobjects.s10_pay_details._
import controllers.ClaimScenarioFactory
import utils.pageobjects.PageObjects

class G1AdditionalInformationIntegrationSpec extends Specification with Tags {
  "Additional information" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/additional-info")
      titleMustEqual("Additional information - Consent and Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/additional-info")
      titleMustEqual("Additional information - Consent and Declaration")
      browser.submit("button[type='submit']")

      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      titleMustEqual("Disclaimer - Consent and Declaration")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      findMustEqualSize("div[class=completed] ul li", 1)
    }
    
    "be presented" in new WithBrowser with PageObjects{
			val page =  G1AdditionalInfoPage(context)
      page goToThePage()
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

      val g2 = page submitPage()
      
      g2 must beAnInstanceOf[G3DisclaimerPage]
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}