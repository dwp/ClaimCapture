package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G3TimeOutsideUKIntegrationSpec extends Specification with Tags {
  "Time outside UK" should {
    "accept the minimal mandatory data" in new WithBrowser {
      FormHelper.fillTimeOutsideUKNotLivingInUK(browser)
      browser.title mustEqual "Claim Date - About You"
    }

    "have a valid date when currently living in UK" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#livingInUK_answer_yes")
      browser.submit("button[value='next']")
      browser.title mustEqual "Time Outside UK - About You"
    }

    "accept a valid date when currently living in UK" in new WithBrowser {
      FormHelper.fillTimeOutsideUK(browser)
      browser.title mustEqual "Claim Date - About You"
    }

  } section "integration"
}