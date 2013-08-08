package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

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
      titleMustEqual("One Who Pays You - Care You Provide")
      browser.fill("#amount") `with` "INVALID"
      browser.submit("button[type='submit']")
      titleMustEqual("One Who Pays You - Care You Provide")
      browser.find("div[class=validation-summary] ol li").size shouldEqual 4
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.moreAboutTheCare(browser)
      browser.find("div[class=completed] ul li").size() shouldEqual 1
    }

    "be able to navigate back" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)
      titleMustEqual("One Who Pays You - Care You Provide")
      browser.click("#backButton")
      titleMustEqual("More about the care you provide - Care You Provide")
    }

    "navigate to Contact Details Of Paying Person after providing all mandatory data" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutTheCare(browser)

      browser.fill("#firstName") `with` "John"
      browser.fill("#surname") `with` "Joe"
      browser.fill("#amount") `with` "44.99"

      browser.click("#startDatePayment_day option[value='3']")
      browser.click("#startDatePayment_month option[value='4']")
      browser.fill("#startDatePayment_year") `with` "1950"

      browser.submit("button[type='submit']")
      titleMustEqual("Contact Details of Paying Person - Care You Provide")
    }
  } section("integration", models.domain.CareYouProvide.id)
}