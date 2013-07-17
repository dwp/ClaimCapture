package controllers.s8_other_money

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import controllers.{ BrowserMatchers, Formulate }
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

    "contain errors on invalid submission" in {
      "no fields selected" in new WithBrowser {
        browser.goTo("/otherMoney/aboutOtherMoney")
        browser.submit("button[type='submit']")

        browser.find("div[class=validation-summary] ol li").size mustEqual 1
      }

      "text field 1 enabled but not filled in" in new WithBrowser {
        browser.goTo("/otherMoney/aboutOtherMoney")
        browser.click("#yourBenefits_answer_yes")
        browser.submit("button[type='submit']")

        browser.find("div[class=validation-summary] ol li").size mustEqual 1
      }

      "text field 2 enabled but not filled in" in new WithBrowser {
        Formulate.claimDate(browser)
        Formulate.moreAboutYou(browser)
        browser.goTo("/otherMoney/aboutOtherMoney")
        browser.click("#yourBenefits_answer_yes")
        browser.fill("#yourBenefits_text1") `with` "Bar"
        browser.submit("button[type='submit']")

        browser.find("div[class=validation-summary] ol li").size mustEqual 1
      }

      "text1 and text2 enabled but neither not filled in" in new WithBrowser {
        Formulate.moreAboutYou(browser)
        browser.goTo("/otherMoney/aboutOtherMoney")
        browser.click("#yourBenefits_answer_yes")
        browser.submit("button[type='submit']")

        browser.find("div[class=validation-summary] ol li").size mustEqual 1
      }
    }

    "navigate to next page on valid submission with all text fields enabled and filled in" in new WithBrowser {
      Formulate.moreAboutYou(browser)
      Formulate.aboutOtherMoney(browser)
      browser.title mustEqual "TODO"
    }.pendingUntilFixed("Need destination page to exist")

    "navigate to next page on valid submission with first mandatory field set to no" in new WithBrowser {
      browser.goTo("/otherIncome/aboutOtherMoney")
      browser.click("#yourBenefits_answer_no")
      browser.submit("button[type='submit']")
      browser.title mustEqual "TODO"
    }.pendingUntilFixed("Need destination page to exist")
  }
}