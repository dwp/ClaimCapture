package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment.{G4SelfEmploymentPensionsAndExpensesPage, G4SelfEmploymentPensionsAndExpensesPageContext}
import utils.pageobjects.TestData
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{G10AboutYouCompletedPage, G3ClaimDatePageContext}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import controllers.s7_employment.G0Employment
import utils.pageobjects.s7_employment.{G0EmploymentPageContext, G0EmploymentPage}


class G4SelfEmploymentPensionsAndExpensesIntegrationSpec extends Specification with Tags {

  "About Self Employment" should {
    "be presented" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      page goToThePage()
    }

    "not be presented if section not visible" in new WithBrowser with G3ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCareAndNotEmployed()
      page goToThePage()

      val employmentHistoryPage = page runClaimWith(claim, G0EmploymentPage.title, waitForPage = true)
      employmentHistoryPage fillPageWith(claim)

      val nextPage = employmentHistoryPage submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
        val claim = new TestData
        page goToThePage()
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 3
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G4SelfEmploymentPensionsAndExpensesPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}