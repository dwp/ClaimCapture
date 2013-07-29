package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment._
import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario

class G7ExpensesWhileAtWorkIntegationSpec extends Specification with Tags {
  "Expenses related to the person you care for while at work" should {
    "be presented" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      page goToThePage ()
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g2 = page submitPage()
      val g7 = g2 goToPage new G7ExpensesWhileAtWorkPage(browser)
      g7.listCompletedForms.size mustEqual 1
    }

    " contain errors on invalid submission missing madatory field" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      val claim = new ClaimScenario
      claim.SelfEmployedCareExpensesNameOfPerson = ""
      page goToThePage ()
      page fillPageWith claim
      val pageWithErrors = page.submitPage(waitForPage = true, waitDuration = 60)
      pageWithErrors.listErrors.size mustEqual 1
      pageWithErrors.listErrors(0).contains("nameOfPerson")
    }
    
    "navigate back to previous page" in new WithBrowser with G6ChildcareProvidersContactDetailsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g7 = page submitPage()
      g7.goBack() must beAnInstanceOf[G6ChildcareProvidersContactDetailsPage]
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G7ExpensesWhileAtWorkPageContext])
    }
  } section "integration"
}