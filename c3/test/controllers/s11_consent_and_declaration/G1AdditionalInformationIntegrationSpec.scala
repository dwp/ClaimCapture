package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate
import controllers.BrowserMatchers
import utils.pageobjects.S11_consent_and_declaration._
import utils.pageobjects.s10_pay_details._
import controllers.ClaimScenarioFactory

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

      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      titleMustEqual("Consent - Consent and Declaration")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      findMustEqualSize("div[class=completed] ul li", 1)
    }
    
    "be presented" in new WithBrowser with G1AdditionalInfoPageContext {
      page goToThePage()
    }
    
    "navigate back to Pay Details - Completed" in new WithBrowser with G3PayDetailsCompletedPageContext {
      page goToThePage()
      val s11g1 = page submitPage()
      
      val s10g3Again = s11g1.goBack()
      
      s10g3Again must beAnInstanceOf[G3PayDetailsCompletedPage]
    }
    
    "present errors if mandatory fields are not populated" in new WithBrowser with G1AdditionalInfoPageContext {
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 1
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G1AdditionalInfoPageContext {
      val claim = ClaimScenarioFactory.s11ConsentAndDeclaration
      page goToThePage()
      page fillPageWith claim

      val g2 = page submitPage()
      
      g2 must beAnInstanceOf[G2ConsentPage]
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}