package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G6RepresentativesForThePersonIntegrationSpec extends Specification with Tags {
  "Representatives For The Person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/representativesForPerson")
      titleMustEqual("Representatives For The Person - Care You Provide")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "contains errors for optional mandatory data" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.click("#actForPerson_yes")
      browser.click("#someoneElseActForPerson_yes")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate back to More About The Person" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      browser.click("#backButton")
      titleMustEqual("More About The Person You Care For - Care You Provide")
    }

    "navigate back to Previous Carer Contact Details" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives For The Person - Care You Provide") // Landed on S4 G6
      browser.click("#backButton")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide") // Back to S4 G5
    }

    "navigate back twice to Previous Carer Personal Details" in new WithBrowser with BrowserMatchers {
      // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives For The Person - Care You Provide") // Landed on S4 G6
      browser.click("#backButton")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")
      browser.click("#backButton")
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide") // Back to S4 G4
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.representativesForThePerson(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}