package controllers.s8_other_money

import org.specs2.mutable.{ Tags, Specification }
import org.specs2.execute.PendingUntilFixed
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s8_other_money.G2MoneyPaidToSomeoneElseForYouPageContext
import utils.pageobjects.s8_other_money.G1AboutOtherMoneyPage
import models.domain.MoneyPaidToSomeoneElseForYou
import utils.pageobjects.s8_other_money.G2MoneyPaidToSomeoneElseForYouPage


class G2MoneyPaidToSomeoneElseForYouIntegrationSpec extends Specification with Tags with PendingUntilFixed {
  "Money Paid To Someone Else For You" should {
    "be presented" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      page goToThePage()
    }
    
    "navigate back to Completion - Employment" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      page goToThePage()
      val backPage = page goBack()
      backPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }
    
    "present errors if mandatory fields are not populated" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      page goToThePage()
      //
      val p  = page.submitPage()
      println(p.listErrors)
      p.listErrors.get.size mustEqual 1
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.aboutExtraMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "contain 1 completed form" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.aboutExtraMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G2MoneyPaidToSomeoneElseForYouPage => p numberSectionsCompleted()  mustEqual 1
        case _ => ko("Next Page is not of the right type.")
      }
    }
  }
}