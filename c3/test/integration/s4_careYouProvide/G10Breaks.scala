package integration.s4_careYouProvide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G10Breaks extends Specification with Tags {
  "Care you provide" should {
    "present breaks" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.title() mustEqual "Has Breaks - Care You Provide"
    }

    """present "completed" when no more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Breaks in Care - Care You Provide"
    }
  } section "integration"
}