package integration.s1_carersallowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G4LivesInGBSpec extends Specification with Tags {

  "LivesInGB" should {
    "be presented" in new WithBrowser {
      browser.goTo("/allowance/livesInGB")
      browser.title() mustEqual "Lives in GB - Carer's Allowance"
    }
  } section "integration"

  "Do you normally live in Great Britain" should {
    "acknowledge yes" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Can you get Carer's Allowance?"
      browser.find("div[class=completed] ul li").get(3).getText must contain("Q4")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.click("#q3-no")
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Can you get Carer's Allowance?"
      browser.find("div[class=completed] ul li").get(3).getText must contain("Q4")
      browser.find("div[class=completed] ul li").get(3).getText must contain("No")
    }

  } section "integration"
}