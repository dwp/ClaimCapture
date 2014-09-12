package controllers.s10_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s9_other_money._
import utils.pageobjects.s10_pay_details.G1HowWePayYouPage
import utils.pageobjects.s10_2_information.G1AdditionalInfoPage
import utils.pageobjects.{PageObjects, PageObjectsContext}

class G1HowWePayYouIntegrationSpec extends Specification with Tags {
  "How we pay you" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("How would you like to get paid? - How we pay you")
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.yourDetails(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)

      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("Additional information - Information")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("How would you like to get paid? - How we pay you")

      browser.submit("button[type='submit']")
      titleMustEqual("How would you like to get paid? - How we pay you")

      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      titleMustEqual("Bank/Building society details - How we pay you")
    }

    /**
     * This test case has been modified to be in line with the new Page Object pattern.
     * Please modify the other test cases when you address them
     */
    "navigate back to Other Statutory Pay - Other Money" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      val OtherStatutoryPage = page goToPage new G1AboutOtherMoneyPage(PageObjectsContext(browser))
      OtherStatutoryPage fillPageWith claim
      OtherStatutoryPage submitPage()

      val howWePayPage = OtherStatutoryPage goToPage new G1HowWePayYouPage(PageObjectsContext(browser))
      val previousPage = howWePayPage goBack()
      previousPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with PageObjects{
			val page =  G1HowWePayYouPage(context)
      val claim = ClaimScenarioFactory.s6PayDetails()
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G1AdditionalInfoPage]
    }

  } section("integration", models.domain.PayDetails.id)
}