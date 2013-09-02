package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment._
import utils.pageobjects.s9_other_money._
import utils.pageobjects.s2_about_you._
import controllers.ClaimScenarioFactory

class G9CompletedIntegrationSpec extends Specification with Tags {

  "Self Employment" should {
    "be presented" in new WithBrowser with G9CompletedPageContext {
      page goToThePage ()
    }

    "not present 'Completed' if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G8AboutYouCompletedPage.title, waitForPage = true, waitDuration = 500)

      val nextPage = page goToPage( throwException = false, page = new G9CompletedPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g2 = page submitPage()
      val g9 = g2 goToPage(new G9CompletedPage(browser))
      g9.listCompletedForms.size mustEqual 1
    }

    "navigate back to previous page" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext  {
      page goToThePage()

      page runClaimWith(ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor, G9CompletedPage.title)

      page.goBack() must beAnInstanceOf[G7ExpensesWhileAtWorkPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G9CompletedPageContext {
      page goToThePage()

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G9CompletedPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}