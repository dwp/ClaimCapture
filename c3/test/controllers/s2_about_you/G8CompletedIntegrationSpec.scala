package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G8CompletedIntegrationSpec extends Specification with Tags {

  "About You" should {
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/completed")
      browser.title mustEqual "Completion - About You"
    }

    """be submitted to "your partner" page.""" in new WithBrowser {
      browser.goTo("/aboutyou/completed")
      browser.submit("button[type='submit']")
      pending("Assert title of next page")
    }
  } section "integration"
}