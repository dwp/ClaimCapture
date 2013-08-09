package controllers.s9_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_other_money._

class G2MoneyPaidToSomeoneElseForYouIntegrationSpec extends Specification with Tags {
  "Money Paid To Someone Else For You" should {
    "be presented" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      page goToThePage()
    }

    "navigate back to G1AboutOtherMoney" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      val g2Page = page goToPage(new G2MoneyPaidToSomeoneElseForYouPage(browser))
      val backPage = g2Page goBack()
      backPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 1
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "contain 1 completed form" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G2MoneyPaidToSomeoneElseForYouPage => p numberSectionsCompleted() mustEqual 1
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "navigate to G3PersonWhoGetsThisMoney when answering yes " in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      claim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim = "yes"

      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G3PersonWhoGetsThisMoneyPage]
    }

    "navigate to G5StatutorySickPay when answering no " in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      claim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim = "no"

      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
    }
  } section("integration", models.domain.OtherMoney.id)
}