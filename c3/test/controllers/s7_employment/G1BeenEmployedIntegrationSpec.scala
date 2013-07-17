package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{WithBrowser, FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G1BeenEmployedIntegrationSpec extends Specification with Tags {
  "Been Employed" should {
    "present" in new WithBrowser{
      browser.goTo("/employment/beenEmployed")

      browser.title mustEqual "Your employment history - Employment"
    }

    "start employment entry" in new WithBrowser {
      browser.goTo("/employment/beenEmployed")

      browser.title mustEqual "Your employment history - Employment"

      browser.click("#beenEmployed_yes")
      browser.submit("button[type='submit']")

      browser.title mustEqual "Details about your job - Employment"
    }

    "continue to self employed" in new WithBrowser {
      pending
      browser.goTo("/employment/beenEmployed")

      browser.title mustEqual "Your employment history - Employment"

      browser.click("#beenEmployed_no")
      browser.submit("button[type='submit']")

      browser.title mustEqual "Details about your job - Employment"
    }



  } section "integration"
}