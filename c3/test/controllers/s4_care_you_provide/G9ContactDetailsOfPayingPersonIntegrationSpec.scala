package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import controllers.{BrowserMatchers, Formulate}
import play.api.test.WithBrowser

class G9ContactDetailsOfPayingPersonIntegrationSpec extends Specification with Tags {
  "Contact details of paying person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)

      browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      titleMustEqual("Contact details of the person who pays you - About the care you provide")
    }

    """be submitted and proceed to "breaks" """ in new WithBrowser with BrowserMatchers {
      skipped("These seem to be the wrong")
      /*browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      browser.submit("button[value='next']")

      titleMustEqual("Breaks in care - About the care you provide")*/
    }

    """be submitted with data, proceed to "breaks" and go back""" in new WithBrowser with BrowserMatchers {
      skipped("These seem to be the wrong")
      /*Formulate.moreAboutTheCare(browser)

      browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      browser.fill("#postcode") `with` "BLAH"
      browser.submit("button[value='next']")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.click("#backButton")
      browser.$("#postcode").getValue mustEqual "BLAH"*/
    }
  } section("integration",models.domain.CareYouProvide.id)
}