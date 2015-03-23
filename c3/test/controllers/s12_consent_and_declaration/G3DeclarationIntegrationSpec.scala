package controllers.s12_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.s12_consent_and_declaration.G3DeclarationPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s7_employment.G1EmploymentPage

class G3DeclarationIntegrationSpec extends Specification with Tags {
  "Declaration" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G3DeclarationPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val page = G3DeclarationPage(context)
      page goToThePage()

      val declarationPage = page submitPage()
      declarationPage.listErrors.size mustEqual 2
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
      page jsCheckEnabled() must beTrue
    }

    "contain errors on invalid submission with employment" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.s7SelfEmployedAndEmployed
      val claimDatePage = G1ClaimDatePage(context) goToThePage()
      claimDatePage fillPageWith claim
      claimDatePage submitPage()

      val employmentPage = G1EmploymentPage(context) goToThePage()
      employmentPage fillPageWith claim
      employmentPage submitPage()

      val page = G3DeclarationPage(context) goToThePage()
      page submitPage()

      page.listErrors.size mustEqual 3
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}