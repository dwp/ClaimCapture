package controllers.s9_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario
import utils.pageobjects.PageObjectException
import utils.pageobjects.s2_about_you.G4ClaimDatePageContext
import utils.pageobjects.s9_other_money._
import play.api.test.WithBrowser

class G5StatutorySickPayIntegrationSpec extends Specification with Tags {

  "Statutory Sick Pay" should {
    "be presented" in new WithBrowser with G5StatutorySickPayPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "had sick pay but missing mandatory field" in new WithBrowser with G5StatutorySickPayPageContext {
        val claim = new ClaimScenario
        claim.OtherMoneyStatutorySickPayHaveYouHadAnyStatutorySickPay = "yes"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
      
      "had sick pay but then invalid postcode" in new WithBrowser with G5StatutorySickPayPageContext {
        val claim = new ClaimScenario
        claim.OtherMoneyStatutorySickPayHaveYouHadAnyStatutorySickPay = "yes"
        claim.OtherMoneyStatutorySickPayEmployersNameEmployers = "Johnny B Good"
        claim.OtherMoneyStatutorySickPayEmployersPostCode = "INVALID"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0).contains("postcode")
      }
    }

    "contain the completed forms" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage ()
      page fillPageWith claim
      val moneyPaidPage = page submitPage ()
      val personContactPage = moneyPaidPage.goToPage(new G5StatutorySickPayPage(browser))
      personContactPage.listCompletedForms.size mustEqual 1
    }

    "navigate back" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage ()
      page fillPageWith claim
      val nextPage = page.submitPage()
      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
      val prevPage = nextPage.goBack()
      prevPage must beAnInstanceOf[G2MoneyPaidToSomeoneElseForYouPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G5StatutorySickPayPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G6OtherStatutoryPayPage]
    }

  } section("integration",models.domain.OtherMoney.id)
}
