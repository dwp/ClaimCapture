package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import controllers.{BrowserMatchers, Formulate}
import play.api.test.WithBrowser

class G5PreviousCarerContactDetailsIntegrationSpec extends Specification with Tags {
  "Previous Carer Contact Details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")

      browser.goTo("/careYouProvide/previousCarerContactDetails")
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")

      browser.goTo("/careYouProvide/previousCarerContactDetails")
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")

      browser.fill("#postcode") `with` "INVALID"
      browser.submit("button[type='submit']")
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to Previous Carer Person Details" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")

      browser.click("#backButton")
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/previousCarerContactDetails")
      browser.submit("button[type='submit']")
      titleMustEqual("Representatives for the person you care for - About the care you provide")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")

      Formulate.previousCarerPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
  } section("integration",models.domain.CareYouProvide.id)
}