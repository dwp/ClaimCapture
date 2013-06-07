package models

import org.specs2.mutable.Specification
import play.api.test.WithBrowser

class HtmlSpec extends Specification {
  "Html tests" should {
    "must have the correct title" in new WithBrowser {

      browser.goTo("/")
      browser.title() mustEqual ("GOV.UK - The best place to find government services and information")
    }
  }
}