package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import utils.pageobjects.s9_other_money._
import controllers.ClaimScenarioFactory
import utils.pageobjects.s10_pay_details.G1HowWePayYouPage

class OtherMoneySpec extends Specification with Tags {

  "OtherMoney Completion" should {
    """be presented""" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      page goToPage(new G8OtherMoneyCompletedPage(browser))
     }

    "contain the completed forms" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      
      val completedPage = page submitPage ()
      
      completedPage must beAnInstanceOf[G8OtherMoneyCompletedPage]
      completedPage.listCompletedForms.size shouldEqual 1
    }

    "navigate back to 'Other Statutory Pay'" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      val g8 = page submitPage ()
      g8 must beAnInstanceOf[G8OtherMoneyCompletedPage]

      val g7Again = g8 goBack ()

      g7Again must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]
    }

    "next button text contains the next section name" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      val completedPage = page submitPage ()
      
      browser.find("button[type='submit']").getText shouldEqual "Continue to pay details"
    }

    "navigate to the Pay Details on clicking continue" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      val completedPage = page submitPage ()
      
      val payDetailsPage = completedPage submitPage ()
      payDetailsPage must beAnInstanceOf[G1HowWePayYouPage]
    }
  } section ("integration", models.domain.OtherMoney.id)
}