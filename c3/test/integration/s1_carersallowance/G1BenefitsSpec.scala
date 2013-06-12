package integration.s1_carersallowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G1BenefitsSpec extends Specification with Tags {

  "Benefits" should {
    "be presented" in new WithBrowser {
      browser.goTo("/")
      browser.title() mustEqual "Benefits - Carer's Allowance"
      browser.find("div[class=carers-allowance]").getText must contain("Q1")
    }

    "allow changing answer" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")

      browser.goTo("/allowance/benefits?changing=true")
      browser.find("#q3-yes").getAttribute("value") mustEqual "true"
    }
  } section "integration"

  "Does the person being cared for get one of required benefits" should {
    "acknowledge yes" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.title() mustEqual "Hours - Carer's Allowance"
      browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
      browser.find("div[class=completed] ul li").get(0).getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-no")
      browser.submit("button[type='submit']")
      browser.title() mustEqual "Hours - Carer's Allowance"
      browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
      browser.find("div[class=completed] ul li").get(0).getText must contain("No")
    }
  } section "integration"
}