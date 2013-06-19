package integration.s4_careYouProvide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G9Breaks extends Specification with Tags {
  "Care you provide for breaks in care" should {
    "present breaks" in new WithBrowser {
      browser.goTo("/careYouProvide/breaks")
      browser.title() mustEqual "Breaks - Care You Provide"
    }


  } section "integration"
}