package controllers

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class ThankYouIntegrationSpec extends Specification with Tags {
  "Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      titleMustEqual("Application complete")
    }

    "show employment and self employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)

      browser.goTo("/thankyou/apply-carers")
      titleMustEqual("Application complete")

      browser.find("employment").getText mustEqual "Your Employment documents"
      browser.find("selfEmployment").getText mustEqual "Your Self-employed documents"
    }

    "show employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      titleMustEqual("Application complete")

      browser.find("#employment").getText shouldEqual "Your Employment documents"
      browser.find("#selfEmployment").size() shouldEqual 0
    }

    "show self employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justSelfEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      titleMustEqual("Application complete")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").getText shouldEqual "Your Self-employed documents"
    }

    "don't show employment messages" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      titleMustEqual("Application complete")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").size() shouldEqual 0

    }
  } section "integration"
}