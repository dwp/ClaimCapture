package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G8OneWhoPaysPersonalDetailsIntegrationSpec extends Specification with Tags {
  "One Who Pays Personal Details Page" should {
    "be presented if someone paid you to care" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      titleMustEqual("Details of the person/organisation who pays you - About the care you provide")
    }

    "be skipped if nobody paid you to care" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCareWithNotPaying(browser)
      titleMustEqual("More about the care you provide - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      browser.goTo("/careYouProvide/oneWhoPaysPersonalDetails")
      titleMustEqual("Details of the person/organisation who pays you - About the care you provide")
      browser.fill("#amount") `with` "INVALID"
      browser.submit("button[type='submit']")
      titleMustEqual("Details of the person/organisation who pays you - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.moreAboutTheCare(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "be able to navigate back" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      titleMustEqual("Details of the person/organisation who pays you - About the care you provide")
      browser.click("#backButton")
      titleMustEqual("More about the care you provide - About the care you provide")
    }

    "navigate to Contact Details Of Paying Person" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      browser.submit("button[type='submit']")
      titleMustEqual("Contact details of the person who pays you - About the care you provide")
    }
  } section("integration",models.domain.CareYouProvide.id)
}