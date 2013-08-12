package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G3MoreAboutYourPartnerIntegrationSpec extends Specification with Tags {

  "More About Your Partner" should {
    "be presented" in new WithBrowser {
      browser.goTo("/your-partner/more-about-your-partner")
      browser.title mustEqual "More about your Partner/Spouse - About your partner/spouse"
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/your-partner/more-about-your-partner")
      browser.submit("button[type='submit']")
      titleMustEqual("More about your Partner/Spouse - About your partner/spouse")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain error on missing separation date when separated" in new WithBrowser with BrowserMatchers {
      browser.goTo("/your-partner/more-about-your-partner")
      browser.click("#separatedFromPartner_yes]")
      browser.submit("button[type='submit']")
      titleMustEqual("More about your Partner/Spouse - About your partner/spouse")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      titleMustEqual("More about your Partner/Spouse - About your partner/spouse")
      browser.click("#backButton")
      titleMustEqual("Contact details - About your partner/spouse")
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      Formulate.moreAboutYourPartnerSeparated(browser)
      titleMustEqual("Person you care for - About your partner/spouse")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      titleMustEqual("More about your Partner/Spouse - About your partner/spouse")
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
  } section("integration", models.domain.YourPartner.id)
}