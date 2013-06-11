package integration.s1_carersallowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G5ApproveSpec extends Specification with Tags {

  "Approve" should {
    "be presented" in new WithBrowser {
      browser.goTo("/allowance/approve")
      browser.title() mustEqual "Can you get Carer's Allowance?"
    }
  } section "integration"


  "Carer's Allowance" should {
    "be approved" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")

      browser.title() mustEqual "Can you get Carer's Allowance?"
      browser.find("div[class=prompt]").size mustEqual 1
      browser.find(".prompt.error").size mustEqual 0

    }

    "be declined" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-no")
      browser.submit("input[type='submit']")

      browser.title() mustEqual "Can you get Carer's Allowance?"
      browser.find("div[class=prompt]").size mustEqual 0
      browser.find(".prompt.error").size mustEqual 1
    }

    "navigate to next section" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.click("#q3-no")
      browser.submit("input[type='submit']")
      browser.title() mustEqual "Can you get Carer's Allowance?"

      browser.submit("input[type='submit']")

      browser.title() mustEqual "Your Details - Carer's Allowance"

    }

  } section "integration"
}