package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G7MoreAboutTheCareIntegrationSpec extends Specification with Tags {
  sequential

  "Representatives For The Person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/more-about-the-care")
      titleMustEqual("More about the care you provide - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/care-you-provide/more-about-the-care")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 3
    }

    "contains errors for optional mandatory data" in new WithBrowser {
      browser.goTo("/care-you-provide/more-about-the-care")
      browser.click("#spent35HoursCaring_yes")
      browser.click("#beforeClaimCaring_answer_yes")
      browser.click("#hasSomeonePaidYou_yes")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back" in new WithBrowser with BrowserMatchers {
      Formulate.representativesForThePerson(browser)
      browser.click("#backButton")
      titleMustEqual("Representatives for the person you care for - About the care you provide")
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.moreAboutTheCare(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "choose no options navigate back twice to Previous Carer Contact Details" in new WithBrowser with BrowserMatchers {
      // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")

      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives for the person you care for - About the care you provide")

      browser.click("#actForPerson_no")
      browser.click("#someoneElseActForPerson_no")
      browser.submit("button[type='submit']")
      titleMustEqual("Representatives for the person you care for - About the care you provide")

      browser.click("#backButton")
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")

      browser.click("#backButton")
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
    }

    "choose yes options navigate back twice to Previous Carer Contact Details" in new WithBrowser with BrowserMatchers {
      // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives for the person you care for - About the care you provide")
      browser.click("#actForPerson_yes")
      browser.click("#actAs option[value='guardian']")
      browser.click("#someoneElseActForPerson_yes")
      browser.click("#someoneElseActAs option[value='attorney']")
      browser.submit("button[type='submit']")
      titleMustEqual("Representatives for the person you care for - About the care you provide") // Landed on S4 G7
      browser.click("#backButton")
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")
      browser.click("#backButton")
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide") // Back to S4 G4
    }
  } section ("integration", models.domain.CareYouProvide.id)
}