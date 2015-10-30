package controllers.s_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.Logger
import utils.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import utils.pageobjects.common.ClaimNotesPage
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.s_consent_and_declaration.GDeclarationPage
import utils.pageobjects.s_disclaimer.GDisclaimerPage
import collection.JavaConversions._
import utils.pageobjects.preview.PreviewPage
import utils.helpers.PreviewField._

class GDeclarationIntegrationSpec extends Specification with Tags {
  "Declaration" should {
    "be presented" in new WithBrowser with BrowserMatchers with PageObjects {
      val page =  GDeclarationPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo(GDeclarationPage.url)
      urlMustEqual(GDeclarationPage.url)

      browser.submit("button[type='submit']")
      urlMustEqual(GDeclarationPage.url)
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "navigate back to Preview page" in new WithBrowser with PageObjects {
      val page =  PreviewPage(context)
      page goToThePage()
      val declarationPage = page submitPage()

      val previewPage = declarationPage goBack()

      previewPage must beAnInstanceOf[PreviewPage]
    }

    "not have name or GDeclarationPage field with optional text" in new WithBrowser with PageObjects{
      val page =  GDeclarationPage(context)
      val claim = new TestData
      claim.ConsentDeclarationSomeoneElseTickBox = "Yes"

      page goToThePage()
      page fillPageWith claim

      page.readLabel("nameOrOrganisation") mustEqual("Your name or organisation")
    }

    "no contact selected in GDeclarationPage field with optional text" in new WithBrowser with PageObjects{
      val page =  GDeclarationPage(context)
      val claim = new TestData
      claim.ConsentDeclarationGettingInformationFromAnyOther = "No"

      page goToThePage()
      page fillPageWith claim

      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
      pageWithErrors.source must contain("validation-message")
    }

    "page contains JS enabled check" in new WithBrowser with PageObjects {
      val page = GDeclarationPage(context)
      page goToThePage()
      page.jsCheckEnabled must beTrue
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}