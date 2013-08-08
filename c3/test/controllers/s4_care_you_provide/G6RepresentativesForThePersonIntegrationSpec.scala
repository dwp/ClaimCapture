package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G6RepresentativesForThePersonIntegrationSpec extends Specification with Tags {
  "Representatives For The Person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/representativesForPerson")
      titleMustEqual("Representatives for the person you care for - About the care you provide")
    }

    "contain errors on invalid submission first yesNo not clicked" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain errors on invalid submission second yesNo not clicked" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contains errors for first optional mandatory data" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.click("#actForPerson_no")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }
    
    "contains errors for second optional mandatory data" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.click("#actForPerson_no")
      browser.click("#someoneElseActForPerson_yes")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to More About The Person" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      browser.click("#backButton")
      titleMustEqual("Relationship and other claims - About the care you provide")
    }

    "navigate back to Previous Carer Contact Details" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives for the person you care for - About the care you provide") // Landed on S4 G6
      browser.click("#backButton")
      titleMustEqual("Contact details of previous or existing carer - About the care you provide") // Back to S4 G5
    }

    "navigate back twice to Previous Carer Personal Details" in new WithBrowser with BrowserMatchers {
      // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives for the person you care for - About the care you provide") // Landed on S4 G6
      browser.click("#backButton")
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")
      browser.click("#backButton")
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide") // Back to S4 G4
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.representativesForThePerson(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section("integration",models.domain.CareYouProvide.id)
}