package integration.s1_carersallowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G2HoursSpec extends Specification with Tags {

  "Hours" should {
    "be presented" in new WithBrowser {
      browser.goTo("/allowance/hours")
      browser.title() mustEqual "Hours - Carer's Allowance"
    }
  } section ("integration")


  "Do you spend 35 hours or more each week caring" should {
    "acknowledge yes" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.title() mustEqual "Hours - Carer's Allowance"
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")

      browser.title() mustEqual "Lives in GB - Carer's Allowance"
      browser.find("div[class=completed] ul li").get(1).getText must contain("Q2")
      browser.find("div[class=completed] ul li").get(1).getText must contain("Yes")
    }
    "acknowledge no" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.title() mustEqual "Hours - Carer's Allowance"
      browser.click("#q3-no")
      browser.submit("input[type='submit']")

      browser.title() mustEqual "Lives in GB - Carer's Allowance"
      browser.find("div[class=completed] ul li").get(1).getText must contain("Q2")
      browser.find("div[class=completed] ul li").get(1).getText must contain("No")
    }
  } section ("integration")
}