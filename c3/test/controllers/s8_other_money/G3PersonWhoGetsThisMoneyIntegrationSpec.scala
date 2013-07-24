package controllers.s8_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario
import utils.pageobjects.PageObjectException
import utils.pageobjects.s2_about_you.G4ClaimDatePageContext
import utils.pageobjects.s8_other_money._
import play.api.test.WithBrowser

class G3PersonWhoGetsThisMoneyIntegrationSpec extends Specification with Tags {

  "Person Who Gets This Money" should {
    "be presented" in new WithBrowser with G3PersonWhoGetsThisMoneyPageContext {
      page goToThePage()
    }

    "be skipped if nobody had any money added to a benefit for you" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      claim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim = "no"
      page goToThePage()
      page fillPageWith claim
      val g3Page = page.submitPage()
      g3Page must not(beAnInstanceOf[G3PersonWhoGetsThisMoneyPage])
    }

    "contain errors on invalid submission" in new WithBrowser with G3PersonWhoGetsThisMoneyPageContext {
      page goToThePage()
      page fillPageWith new ClaimScenario
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 2
    }

    "contain the completed forms" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      claim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim = "yes"
      page goToThePage()
      page fillPageWith claim
      val g3Page = page submitPage()
      g3Page must beAnInstanceOf[G3PersonWhoGetsThisMoneyPage]
      g3Page.listCompletedForms.size mustEqual 1
    }

    "navigate back to Money Paid - Other Money" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      claim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim = "yes"
      page goToThePage()
      page fillPageWith claim
      val g3Page = page submitPage()
      val previousPage = g3Page.goBack()
      previousPage must beAnInstanceOf[G2MoneyPaidToSomeoneElseForYouPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G3PersonWhoGetsThisMoneyPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G4PersonContactDetailsPage]
    }

  } section "integration"

}
