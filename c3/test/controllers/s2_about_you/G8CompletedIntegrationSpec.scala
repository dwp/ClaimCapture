package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

class G8CompletedIntegrationSpec extends Specification with Tags {

  "About You" should {
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/completed")
      browser.title mustEqual "Completion - About You"
    }
    
    """show the text "Continue to Partner / Spouse" on the submit button when next section is "Your Partner"""" in new WithBrowser {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.propertyAndRent(browser)
      
      browser.find("#submit").getText mustEqual "Continue to Partner/Spouse"
    }
    
    """show the text "Continue to Care You Provide" on the submit button when next section is "Care You Provide"""" in new WithBrowser {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      Formulate.employment(browser)
      Formulate.propertyAndRent(browser)
      
      browser.find("#submit").getText mustEqual "Continue to care you provide"
    }

    """be submitted to "Personal Details - Your Partner" page when they have had a partner/spouse at any time since the claim date""" in new WithBrowser {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.propertyAndRent(browser)
      
      browser.submit("button[type='submit']")
      
      browser.title mustEqual "Personal Details - Your Partner"  
    }

    """be submitted to "Their Personal Details - Care You Provide" page when they have NOT had a partner/spouse at any time since the claim date""" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      titleMustEqual("Contact Details - About You")(Duration(300, TimeUnit.SECONDS))

      Formulate.yourContactDetails(browser)
      titleMustEqual("Claim Date - About You")(Duration(300, TimeUnit.SECONDS))

      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")(Duration(300, TimeUnit.SECONDS))

      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      titleMustEqual("Employment - About You")(Duration(300, TimeUnit.SECONDS))

      Formulate.employment(browser)
      titleMustEqual("Property and Rent - About You")(Duration(300, TimeUnit.SECONDS))

      Formulate.propertyAndRent(browser)
      titleMustEqual("Completion - About You")(Duration(300, TimeUnit.SECONDS))

      browser.submit("button[type='submit']")
      titleMustEqual("Their Personal Details - Care You Provide")(Duration(300, TimeUnit.SECONDS))
    }
  } section "integration"
}