package controllers

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class ThankYouIntegrationSpec extends Specification with Tags {
  "Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      titleMustEqual("Application complete")
    }
  } section "integration"

  "Change Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/change-carers")
      titleMustEqual("Change complete")
    }
  } section "integration"
}