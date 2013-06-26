package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper

class G9ContactDetailsOfPayingPersonIntegrationSpec extends Specification with Tags {
  "Contact details of paying person" should {
    "be presented" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)

      browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      browser.title() mustEqual "Contact Details of Paying Person - Care You Provide"
    }

    """be submitted and proceed to "breaks" """ in new WithBrowser {
      browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      browser.submit("button[value='next']")

      browser.title() mustEqual "Has Breaks - Care You Provide"
    }

    """be submitted with data, proceed to "breaks" and go back""" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)

      browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      browser.fill("#postcode") `with` "BLAH"
      browser.submit("button[value='next']")
      browser.title() mustEqual "Has Breaks - Care You Provide"

      browser.click(".form-steps a")
      browser.$("#postcode").getValue mustEqual "BLAH"
    }
  } section "integration"
}