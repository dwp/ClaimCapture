package integration.s4_careYouProvide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G10Breaks extends Specification with Tags {
  "Care you provide for breaks in care" should {
    "present breaks" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.title() mustEqual "Has Breaks - Care You Provide"
    }


  } section "integration"
}