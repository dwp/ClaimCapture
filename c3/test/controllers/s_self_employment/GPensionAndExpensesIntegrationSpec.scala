package controllers.s_self_employment

import org.specs2.mutable._
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s_self_employment._
import utils.pageobjects.PageObjects
import utils.pageobjects.s_employment.GEmploymentAdditionalInfoPage

class GPensionAndExpensesIntegrationSpec extends Specification {

  section("integration",models.domain.Employed.id)
  "Self Employment Pension And Expenses" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GSelfEmploymentPensionsAndExpensesPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val page =  GSelfEmploymentPensionsAndExpensesPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GSelfEmploymentPensionsAndExpensesPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val page =  GSelfEmploymentPensionsAndExpensesPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GEmploymentAdditionalInfoPage]
    }

    "be able to navigate back to a completed form" in new WithBrowser with PageObjects {
			val page =  GSelfEmploymentPensionsAndExpensesPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[GSelfEmploymentPensionsAndExpensesPage]
    }
  }
  section("integration",models.domain.Employed.id)
}
