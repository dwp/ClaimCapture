package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper
import java.util.concurrent.TimeUnit

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """present "completion".""" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      browser.await().atMost(30, TimeUnit.SECONDS).until("title").equals("Their Contact Details - Care You Provide")

      browser.goTo("/careYouProvide/completed")
      browser.await().atMost(30, TimeUnit.SECONDS).until("title").equals("Completed - Care You Provide")
    }

    /*"""restart when attempting to "complete" with missing "question groups".""" in new WithBrowser {
      browser.goTo("/careYouProvide/completed")
    }*/

    /*"""restart when attempting to "complete" with missing "question groups".""" in new WithBrowser {
      browser.goTo("/aboutyou/completed")
      browser.submit("button[type='submit']")
      pending("Assert title of next page")
    }*/
  } section "integration"
}