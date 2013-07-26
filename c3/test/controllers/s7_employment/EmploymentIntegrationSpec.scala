package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class EmploymentIntegrationSpec extends Specification with Tags {
  "Employment" should {
    "present completion" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/completed")
      titleMustEqual("Completion - Employment")
    }

    """progress to "self employed".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/completed")
      browser.submit("button[type='submit']")
      titleMustEqual("About Other Money - Other Money")
      pending("Don't think this is right")
    }

    """go back to start of employment i.e. "employment history".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/completed")

      browser.click("#backButton")
      titleMustEqual("Your employment history - Employment")
    }
  } section "integration"
}