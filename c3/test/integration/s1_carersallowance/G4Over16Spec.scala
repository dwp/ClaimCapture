package integration.s1_carersallowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G4Over16Spec extends Specification with Tags {

  "Over 16" should {
    "be presented" in new WithBrowser {
      browser.goTo("/allowance/over16")
      browser.title() mustEqual "Over 16 - Carer's Allowance"
    }
  } section "integration"


  "Are you aged 16 or over" should {
    "acknowledge yes" in new WithBrowser {
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
      browser.find("div[class=completed] ul li").get(3).getText must contain("Q4")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowser {
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
      browser.find("div[class=completed] ul li").get(3).getText must contain("Q4")
      browser.find("div[class=completed] ul li").get(3).getText must contain("No")
    }

  } section "integration"
}