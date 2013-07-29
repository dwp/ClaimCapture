package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

class G3MoreAboutYourPartnerIntegrationSpec extends Specification with Tags {

  "More About Your Partner" should {
    "be presented" in new WithBrowser {
      browser.goTo("/yourPartner/moreAboutYourPartner")
      browser.title mustEqual "More About Your Partner - Your Partner"
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/yourPartner/moreAboutYourPartner")
      browser.submit("button[type='submit']")
      titleMustEqual("More About Your Partner - Your Partner")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain error on missing separation date when separated" in new WithBrowser with BrowserMatchers {
      browser.goTo("/yourPartner/moreAboutYourPartner")
      browser.click("#separatedFromPartner_yes]")
      browser.submit("button[type='submit']")
      titleMustEqual("More About Your Partner - Your Partner")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      titleMustEqual("More About Your Partner - Your Partner")(Duration(60, TimeUnit.SECONDS))
      browser.click("#backButton")
      titleMustEqual("Contact Details - Your Partner")(Duration(60, TimeUnit.SECONDS))
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      Formulate.moreAboutYourPartnerSeparated(browser)
      titleMustEqual("Person You Care For - Your Partner")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      titleMustEqual("More About Your Partner - Your Partner")
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
  } section "integration"
}