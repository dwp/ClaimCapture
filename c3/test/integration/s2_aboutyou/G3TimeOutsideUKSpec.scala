package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G3TimeOutsideUKSpec extends Specification with Tags {
  "Time outside UK" should {
    "accept the minimal mandatory data" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_yes")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Claim Date - About You"
    }

    "have a valid date when not currently living in UK" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_no")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Time Outside UK - About You"
    }

    "accept a valid date when not currently living in UK" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_no")

      browser.click("#arrivedInUK-day option[value='1']")
      browser.click("#arrivedInUK-month option[value='1']")
      browser.fill("#arrivedInUK-year") `with` "2001"

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
      browser.click("#currentlyLivingInUK_yes")
      browser.click("#planToGoBack_yes")

      browser.click("#whenPlanToGoBack-day option[value='1']")
      browser.click("#whenPlanToGoBack-month option[value='1']")
      browser.fill("#whenPlanToGoBack-year") `with` "2001"

      browser.submit("button[value='next']")
      browser.title() mustEqual "Claim Date - About You"
    }
  } section "integration"
}