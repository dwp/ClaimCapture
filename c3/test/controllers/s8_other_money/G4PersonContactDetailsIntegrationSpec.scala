package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_other_money.PersonContactDetailsPageContext

class G4PersonContactDetailsIntegrationSpec extends Specification with Tags {

  "Person Contact Details" should {
    "be presented" in new WithBrowser with PersonContactDetailsPageContext {
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/yourPartner/contactDetails")
      browser.fill("#postcode") `with` "INVALD"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser {
      //      Formulate.yourPartnerPersonalDetails(browser)
      //      browser.find("div[class=completed] ul li").size() mustEqual 1
      pending
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser {
      //      browser.goTo("/yourPartner/contactDetails")
      //      browser.click("#backButton")
      //      browser.title mustEqual "Personal Details - Your Partner"
      pending
    }

    "navigate to next page on valid submission" in new WithBrowser {
      pending
    }

  } section "integration"
}
