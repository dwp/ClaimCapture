package controllers.s10_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s9_other_money._
import utils.pageobjects.s10_pay_details.G1HowWePayYouPage

class G1HowWePayYouIntegrationSpec extends Specification with Tags {
  "How we pay you" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("How would you like to get paid? - How we pay you")
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("Your nationality and residency - About you - the carer")

      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)

      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("Additional information - Consent and Declaration")
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
    "navigate back to Other Statutory Pay - About Other Money" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      val OtherStatutoryPage = page goToPage new G6OtherStatutoryPayPage(browser)
      OtherStatutoryPage fillPageWith claim
      OtherStatutoryPage submitPage()

      val howWePayPage = OtherStatutoryPage goToPage new G1HowWePayYouPage(browser)
      val previousPage = howWePayPage goBack()
      previousPage must beAnInstanceOf[G6OtherStatutoryPayPage]
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      findMustEqualSize("div[class=completed] ul li", 1)
    }
  } section("integration", models.domain.PayDetails.id)
}