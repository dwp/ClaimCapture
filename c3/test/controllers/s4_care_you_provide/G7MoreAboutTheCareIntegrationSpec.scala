package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G7MoreAboutTheCareIntegrationSpec extends Specification with Tags {

  "Representatives For The Person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/moreAboutTheCare")
      titleMustEqual("More about the care you provide - Care You Provide")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/moreAboutTheCare")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 3
    }

    "contains errors for optional mandatory data" in new WithBrowser {
      browser.goTo("/careYouProvide/moreAboutTheCare")
      browser.click("#spent35HoursCaring_yes")
      browser.click("#beforeClaimCaring_answer_yes")
      browser.click("#hasSomeonePaidYou_yes")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back" in new WithBrowser with BrowserMatchers {
      Formulate.representativesForThePerson(browser)
      browser.click("#backButton")
      titleMustEqual("Representatives For The Person - Care You Provide")
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.moreAboutTheCare(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "choose no options navigate back twice to Previous Carer Contact Details" in new WithBrowser with BrowserMatchers {
      // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      browser.click("#actForPerson_no")
      browser.click("#someoneElseActForPerson_no")
      browser.submit("button[type='submit']")
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.click("#backButton")
      titleMustEqual("Representatives For The Person - Care You Provide")

      browser.click("#backButton")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")
    }.pendingUntilFixed("GET A TIMEOUT")

    "choose yes options navigate back twice to Previous Carer Contact Details" in new WithBrowser with BrowserMatchers {
      // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      browser.click("#actForPerson_yes")
      browser.click("#actAs option[value='guardian']")
      browser.click("#someoneElseActForPerson_yes")
      browser.click("#someoneElseActAs option[value='attorney']")
      browser.submit("button[type='submit']")
      titleMustEqual("More about the care you provide - Care You Provide") // Landed on S4 G7
      browser.click("#backButton")
      browser.click("#backButton")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide") // Back to S4 G4
    }.pendingUntilFixed("GET A TIMEOUT")
  }
}