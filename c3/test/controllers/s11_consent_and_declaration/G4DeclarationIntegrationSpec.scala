package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.S11_consent_and_declaration.G4DeclarationPage

class G4DeclarationIntegrationSpec extends Specification with Tags {
  "Declaration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/declaration")
      titleMustEqual("Declaration - Consent and Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/declaration")
      titleMustEqual("Declaration - Consent and Declaration")

      browser.submit("button[type='submit']")
      titleMustEqual("Declaration - Consent and Declaration")
      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    "navigate back to Disclaimer" in new WithBrowser with BrowserMatchers {
      Formulate.disclaimer(browser)
      titleMustEqual("Declaration - Consent and Declaration")

      browser.click(".form-steps a")
      titleMustEqual("Disclaimer - Consent and Declaration")
    }

    "not have name or organisation field with optional text" in new WithBrowser with PageObjects{
      val page =  G4DeclarationPage(context)
      val claim = new TestData
      claim.ConsentDeclarationSomeoneElseTickBox = "Yes"

      page goToThePage()
      page fillPageWith claim

      page.readLabel("nameOrOrganisation") mustEqual("Your name and/or organisation")
    }

    "page contains JS enabled check" in new WithBrowser with PageObjects {
      val page = G4DeclarationPage(context)
      page goToThePage()
      page jsCheckEnabled() must beTrue
    }

    "contain errors on invalid submission with employment" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      browser.goTo("/consent-and-declaration/declaration")
      titleMustEqual("Declaration - Consent and Declaration")

      browser.submit("button[type='submit']")
      titleMustEqual("Declaration - Consent and Declaration")
      findMustEqualSize("div[class=validation-summary] ol li", 3)
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}