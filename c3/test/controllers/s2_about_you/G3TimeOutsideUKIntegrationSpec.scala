package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G3TimeOutsideUKIntegrationSpec extends Specification with Tags {
  "Time outside UK" should {
    "accept the minimal mandatory data" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_no")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Claim Date - About You"
    }

    "have a valid date when currently living in UK" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_yes")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Time Outside UK - About You"
    }

    "accept a valid date when currently living in UK" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_yes")

      browser.click("#arrivedInUK_day option[value='1']")
      browser.click("#arrivedInUK_month option[value='1']")
      browser.fill("#arrivedInUK_year") `with` "2001"

      browser.submit("button[value='next']")
      browser.title() mustEqual "Claim Date - About You"
    }

    "have a valid date when planning to go back 'home'" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_yes")
      browser.click("#planToGoBack_yes")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Time Outside UK - About You"
    }

    "accept a valid date when planning to go back 'home'" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_no")
      browser.click("#planToGoBack_yes")

      browser.click("#whenPlanToGoBack_day option[value='1']")
      browser.click("#whenPlanToGoBack_month option[value='1']")
      browser.fill("#whenPlanToGoBack_year") `with` "2001"

      browser.submit("button[value='next']")
      browser.title() mustEqual "Claim Date - About You"
    }
  } section "integration"
}