package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s8_self_employment._
import utils.pageobjects.PageObjects
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s7_employment.G9EmploymentAdditionalInfoPage

class G4PensionAndExpensesIntegrationSpec extends Specification with Tags {
  "Self Employment Pension And Expenses" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G4SelfEmploymentPensionsAndExpensesPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects{
      val page =  G4SelfEmploymentPensionsAndExpensesPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G4SelfEmploymentPensionsAndExpensesPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
      val page =  G4SelfEmploymentPensionsAndExpensesPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]
    }

    "be able to navigate back to a completed form" in new WithBrowser  with PageObjects{
			val page =  G4SelfEmploymentPensionsAndExpensesPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G4SelfEmploymentPensionsAndExpensesPage]
    }
  } section("integration",models.domain.Employed.id)
}