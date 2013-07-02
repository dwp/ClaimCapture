package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G1YourDetailsIntegrationSpec extends Specification with Tags {

  "Your Details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.title() mustEqual "Your Details - About You"
    }

    "navigate back to approve page" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.click(".form-steps a")
      browser.title() mustEqual "Can you get Carer's Allowance?"
    }
  } section "integration"
}