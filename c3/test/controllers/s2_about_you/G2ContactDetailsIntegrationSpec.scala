package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G2ContactDetailsIntegrationSpec extends Specification with Tags {

  "Contact Details" should {

    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/contactDetails")
      browser.title() mustEqual "Contact Details - About You"
    }

    "contain 1 completed form" in new WithBrowser {
      FormHelper.fillYourDetails(browser)

      browser.title() mustEqual "Contact Details - About You"
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "be able to navigate back to a completed form" in new WithBrowser {
      FormHelper.fillYourDetails(browser)

      browser.title() mustEqual "Contact Details - About You"

      browser.click("div[class=completed] ul li a")
      browser.title() mustEqual "Your Details - About You"
    }
  } section "integration"
}