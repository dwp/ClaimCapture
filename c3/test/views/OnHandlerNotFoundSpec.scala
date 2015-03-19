package views

import org.specs2.mutable.Specification
import play.api.test.WithBrowser

class OnHandlerNotFoundSpec extends Specification {
  "OnHandlerNotFound" should {
    "show 404 page when user navigates to a bad URL" in new WithBrowser {
      browser.goTo("/404")
      browser.title mustEqual "This page can't be found"
    }
  }
}
