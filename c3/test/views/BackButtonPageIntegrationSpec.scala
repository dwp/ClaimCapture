package views

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithApplication
import play.api.test.WithBrowser
import scala.collection.JavaConversions._


class BackButtonPageIntegrationSpec extends Specification with Tags {

  "Back button page" should {
    "show if browser back button clicked in Thank You page" in new WithBrowser{
      browser goTo "/allowance/benefits"
      browser title() mustEqual "Does the person you care for get one of these benefits? - Can you get Carer's Allowance?"

      browser goTo "/thankyou/apply-carers"
      browser title() mustEqual "Application complete"

      browser.getCookies.toSet.exists(cookie => cookie.getName == "application-finished" && cookie.getValue == "true") must beTrue

      browser executeScript "window.history.back()"

      browser url() mustEqual "/back-button-page"
    }
  }

}
