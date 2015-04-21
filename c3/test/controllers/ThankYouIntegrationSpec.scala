package controllers

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class ThankYouIntegrationSpec extends Specification with Tags {
  "Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")
    }

    "show employment and self employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("employment").getText mustEqual "Your Employment documents"
      browser.find("selfEmployment").getText mustEqual "Your Self-employed documents"
    }

    "show employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").getText.nonEmpty must beTrue
      browser.find("#selfEmployment").size() shouldEqual 0
    }

    "show self employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justSelfEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").getText.nonEmpty must beTrue
    }

    "don't show employment messages" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").size() shouldEqual 0

    }
  } section "integration"
}