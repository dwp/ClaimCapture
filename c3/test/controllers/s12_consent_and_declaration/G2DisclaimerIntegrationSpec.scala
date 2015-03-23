package controllers.s12_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate
import controllers.BrowserMatchers
import utils.pageobjects.PageObjects
import utils.pageobjects.s10_information.G1AdditionalInfoPage
import utils.pageobjects.s12_consent_and_declaration.{G3DeclarationPage, G2DisclaimerPage}

class G2DisclaimerIntegrationSpec extends Specification with Tags {
  "Disclaimer" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G2DisclaimerPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val page = G2DisclaimerPage(context)
      page goToThePage()
      val samePage = page submitPage()
      samePage.listErrors.nonEmpty must beTrue
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.disclaimer(browser)
      urlMustEqual(G3DeclarationPage.url)
    }

    "navigate back to additional information" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      Formulate.additionalInfo(browser)
      browser.click("#backButton")
      urlMustEqual(G1AdditionalInfoPage.url)
    }

  } section("integration", models.domain.ConsentAndDeclaration.id)
}