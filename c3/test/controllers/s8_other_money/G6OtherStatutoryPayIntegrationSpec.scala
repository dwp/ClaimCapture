package controllers.s8_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s8_other_money._
import play.api.test.WithBrowser

class G6OtherStatutoryPayIntegrationSpec extends Specification with Tags {

  "Other Statutory Pay - Other Money" should {
    "be presented" in new WithBrowser with G6OtherStatutoryPayPageContext {
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with G6OtherStatutoryPayPageContext {
      val claim = new ClaimScenario
      claim.OtherMoneyHaveYouSMPSinceClaim = "yes"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser with G5StatutorySickPayPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim
      val otherStatutoryPayPage = page submitPage()
      otherStatutoryPayPage.listCompletedForms.size mustEqual 1
    }

    "navigate back to previous page" in new WithBrowser with G5StatutorySickPayPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith(claim)
      val nextPage = page.submitPage()
      nextPage must beAnInstanceOf[G6OtherStatutoryPayPage]

      nextPage.goBack() must beAnInstanceOf[G5StatutorySickPayPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G6OtherStatutoryPayPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G6OtherStatutoryPayPage])
    }

  } section "integration"
}
