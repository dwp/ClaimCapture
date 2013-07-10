package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G3Over16IntegrationSpec extends Specification with Tags {

  "Over 16" should {
    "be presented" in new WithBrowser {
      browser.goTo("/allowance/over16")
      browser.title mustEqual "Over 16 - Carer's Allowance"
    }
  } section "integration"

  "Are you aged 16 or over" should {
    "acknowledge yes" in new WithBrowser with BrowserMatchers {
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

      browser.find("div[class=completed] ul li").get(2).getText must contain("Q3")
      browser.find("div[class=completed] ul li").get(2).getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowser with BrowserMatchers {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Over 16 - Carer's Allowance")

      browser.click("#q3-no")
      browser.submit("button[type='submit']")
      titleMustEqual("Lives in GB - Carer's Allowance")

      browser.find("div[class=completed] ul li").get(2).getText must contain("Q3")
      browser.find("div[class=completed] ul li").get(2).getText must contain("No")
    }
  } section "integration"
}