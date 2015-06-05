package controllers.s12_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.Logger
import utils.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import utils.pageobjects.common.ClaimNotesPage
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.s12_consent_and_declaration.G3DeclarationPage
import utils.pageobjects.s1_disclaimer.G1DisclaimerPage
import collection.JavaConversions._
import utils.pageobjects.preview.PreviewPage

class G3DeclarationIntegrationSpec extends Specification with Tags {
  "Declaration" should {
    "be presented" in new WithBrowser with BrowserMatchers with PageObjects {
      val page =  G3DeclarationPage(context)
      page goToThePage()
    }

    "Display claim notes when click on claim notes link" in new WithBrowser with PageObjects {
      val page =  G3DeclarationPage(context)
      page goToThePage()
      val handles1 = context.browser.webDriver.getWindowHandles
      val claimNotesPage = page.clickLinkOrButton("#claimnotes")
      claimNotesPage must beAnInstanceOf[ClaimNotesPage]
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo(G3DeclarationPage.url)
      urlMustEqual(G3DeclarationPage.url)

      browser.submit("button[type='submit']")
      urlMustEqual(G3DeclarationPage.url)
      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    "navigate back to Preview page" in new WithBrowser with PageObjects {
      val page =  PreviewPage(context)
      page goToThePage()
      val declarationPage = page submitPage()

      val previewPage = declarationPage goBack()

      previewPage must beAnInstanceOf[PreviewPage]
    }

    "not have name or G3DeclarationPage field with optional text" in new WithBrowser with PageObjects{
      val page =  G3DeclarationPage(context)
      val claim = new TestData
      claim.ConsentDeclarationSomeoneElseTickBox = "Yes"

      page goToThePage()
      page fillPageWith claim

      page.readLabel("nameOrOrganisation") mustEqual("Your name and/or organisation")
    }

    "page contains JS enabled check" in new WithBrowser with PageObjects {
      val page = G3DeclarationPage(context)
      page goToThePage()
      page.jsCheckEnabled must beTrue
    }

    "contain errors on invalid submission with employment" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      browser.goTo(G3DeclarationPage.url)
      urlMustEqual(G3DeclarationPage.url)

      browser.submit("button[type='submit']")
      urlMustEqual(G3DeclarationPage.url)
      findMustEqualSize("div[class=validation-summary] ol li", 3)
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}