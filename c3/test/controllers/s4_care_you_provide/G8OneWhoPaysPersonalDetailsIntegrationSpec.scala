package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper

class G8OneWhoPaysPersonalDetailsIntegrationSpec extends Specification with Tags {

  "One Who Pays Personal Details Page" should {
    "be presented if someone paid you to care" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)
      browser.title() mustEqual "One Who Pays You - Care You Provide"
    }

    "be skipped if nobody paid you to care" in new WithBrowser {
      Helper.fillMoreAboutTheCareWithNotPaying(browser)
      browser.title() mustNotEqual "One Who Pays You - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)
      browser.goTo("/careYouProvide/oneWhoPaysPersonalDetails")
      browser.fill("#amount") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "be able to navigate back" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)
      browser.title() mustEqual "One Who Pays You - Care You Provide"
      browser.click("#backButton")
      browser.title() mustEqual "More about the care you provide - Care You Provide"
    }

    "navigate to Contact Details Of Paying Person" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)
      browser.submit("button[type='submit']")
      browser.title() mustEqual "Contact Details of Paying Person - Care You Provide"
    }

  } section "integration"
}