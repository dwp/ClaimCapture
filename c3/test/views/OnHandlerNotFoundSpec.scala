package views

import org.specs2.mutable.Specification
import scala.xml.NodeSeq
import models.domain._
import play.api.test.WithBrowser
import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import play.api.test.WithApplication
import play.api.test.FakeRequest

class OnHandlerNotFoundSpec extends Specification {
  "OnHandlerNotFound" should {
    "show 404 page when user navigates to a bad URL" in new WithBrowser {
      browser.goTo("/404")
      browser.title mustEqual "GOV.UK - The best place to find government services and information"
    }
  }
}
