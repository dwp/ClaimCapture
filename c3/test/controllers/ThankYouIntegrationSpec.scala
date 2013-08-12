package controllers

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class ThankYouIntegrationSpec extends Specification with Tags {
  "Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou")
      titleMustEqual("GOV.UK - The best place to find government services and information")
    }
  } section "integration"
}