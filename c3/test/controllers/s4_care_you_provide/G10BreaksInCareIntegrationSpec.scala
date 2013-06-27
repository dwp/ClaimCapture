package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G10BreaksInCareIntegrationSpec extends Specification with Tags {
  "Has breaks" should {
    "present" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.title() mustEqual "Breaks in Care - Care You Provide"
    }

    """present "completed" when no more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.click("#answer_no")
      browser.submit("button[value='next']")
      browser.pageSource() must contain("Completed - Care You Provide")
    }

    "go back to contact details" in new WithBrowser {
      pending("Once 'Contact details' are done, this example must be written")
    }
  } section "integration"
}