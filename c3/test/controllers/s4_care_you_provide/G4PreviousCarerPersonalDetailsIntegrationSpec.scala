package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers, Formulate}

class G4PreviousCarerPersonalDetailsIntegrationSpec extends Specification with Tags {
  "Previous Carer Personal Details" should {
    "navigate to Previous Carer Details, if anyone else claimed allowance for this person before" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
    }

    "navigate to Representatives For The Person, if nobody claimed allowance for this person before" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/previous-carer-personal-details")
      titleMustEqual("Representatives for the Person you care for - About the care you provide")
    }

    "highlight missing data" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
      next
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate to Previous Carer Contact Details on submission of completed form" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def fill() = {
        browser fill "#firstName" `with` "Rip"
        browser fill "#middleName" `with` "Van"
        browser fill "#surname" `with` "Winkle"
        browser fill "#nationalInsuranceNumber_ni1" `with` "12345"
        browser fill "#nationalInsuranceNumber_ni2" `with` "12"
        browser fill "#nationalInsuranceNumber_ni3" `with` "34"
        browser fill "#nationalInsuranceNumber_ni4" `with` "56"
        browser fill "#nationalInsuranceNumber_ni5" `with` "C"
        browser click "#dateOfBirth_day option[value='3']"
        browser click "#dateOfBirth_month option[value='4']"
        browser fill "#dateOfBirth_year" `with` "1950"
      }

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")

      fill()
      next
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to More About The Person you care for" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      back
      titleMustEqual("Relationship and other claims - About the care you provide")
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "navigating forward and back presents the same completed question list" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
      browser.find("div[class=completed] ul li").size mustEqual 3

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")
      browser.find("div[class=completed] ul li").size mustEqual 4

      back
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")
      browser.find("div[class=completed] ul li").size mustEqual 3
    }
  } section("integration", models.domain.CareYouProvide.id)
}