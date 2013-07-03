package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """present "completion".""" in new WithBrowser {
      browser.goTo("/careYouProvide/completed")
      browser.title mustEqual "Completion - Care You Provide"
    }

    """restart when attempting to "complete" with missing "question groups".""" in new WithBrowser {
      browser.goTo("/careYouProvide/completed")
    }

    /*"""restart when attempting to "complete" with missing "question groups".""" in new WithBrowser {
      browser.goTo("/aboutyou/completed")
      browser.submit("button[type='submit']")
      pending("Assert title of next page")
    }*/
  } section "integration"
}