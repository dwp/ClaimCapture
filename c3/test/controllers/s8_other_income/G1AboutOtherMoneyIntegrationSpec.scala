package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import org.specs2.execute.PendingUntilFixed

class G1AboutOtherMoneyIntegrationSpec extends Specification with Tags with PendingUntilFixed {
  "About Other Money" should {
    "be presented" in new WithBrowser {
      browser.goTo("/otherMoney/aboutOtherMoney")
      browser.title mustEqual "About Other Money - Other Money"
    }
    
    "navigate back to Completion - Employment" in new WithBrowser {
      browser.goTo("/otherMoney/aboutOtherMoney")
      browser.click("#backButton")
      pending("Need Completion - Employment to exist")
      browser.title mustEqual "Details about your job - Employment"
    }
    
    "navigate to next page on valid submission with all fields filled in" in new WithBrowser {
      Formulate.aboutOtherMoney(browser)
      browser.title mustEqual "TODO"
    }.pendingUntilFixed("Need destination page to exist")
    
    "navigate to next page on valid submission with only mandatory fields filled in" in new WithBrowser {
      browser.goTo("/otherIncome/aboutOtherMoney")
      browser.click("#yourBenefits_yes")
      browser.submit("button[type='submit']")
      browser.title mustEqual "TODO"
    }.pendingUntilFixed("Need destination page to exist")
  }
}