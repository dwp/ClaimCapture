package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

class G8OneWhoPaysPersonalDetailsIntegrationSpec extends Specification with Tags {
  "One Who Pays Personal Details Page" should {
    "be presented if someone paid you to care" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      titleMustEqual("One Who Pays You - Care You Provide")
    }

    "be skipped if nobody paid you to care" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCareWithNotPaying(browser)
      titleMustEqual("More about the care you provide - Care You Provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      browser.goTo("/careYouProvide/oneWhoPaysPersonalDetails")
      titleMustEqual("One Who Pays You - Care You Provide")(Duration(10, TimeUnit.MINUTES))
      browser.fill("#amount") `with` "INVALID"
      browser.submit("button[type='submit']")
      titleMustEqual("One Who Pays You - Care You Provide")(Duration(10, TimeUnit.MINUTES))
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.moreAboutTheCare(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "be able to navigate back" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      titleMustEqual("One Who Pays You - Care You Provide")
      browser.click("#backButton")
      titleMustEqual("More about the care you provide - Care You Provide")
    }

    "navigate to Contact Details Of Paying Person" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      browser.submit("button[type='submit']")
      titleMustEqual("Contact Details of Paying Person - Care You Provide")
    }
  } section "integration"
}