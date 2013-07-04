package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

class NavigationSpec extends Specification with Tags {
  "Browser" should {
    "not cache pages" in new WithBrowserAndMatchers {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Over 16 - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Lives in GB - Carer's Allowance")

      browser.webDriver.navigate().back()
      browser.webDriver.navigate().back()
      browser.webDriver.navigate().back()
      titleMustEqual("Benefits - Carer's Allowance")

      browser.click("#q3-no")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")

      val completed = browser.find("div[class=completed] ul li")
      completed.size() mustEqual 1
      completed.getText must contain("Q1")
    }
  } section "integration"
}