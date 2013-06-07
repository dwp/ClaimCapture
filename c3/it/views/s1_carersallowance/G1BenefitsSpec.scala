package views.s1_carersallowance

import org.specs2.mutable.Specification
import play.api.test.WithBrowser

class G1BenefitsSpec extends Specification {

  "Benefits" should {
    "be presented" in new WithBrowser {
      browser.goTo("/")
      browser.title() mustEqual "Benefits - Carers Allowance"
      browser.find("div[class=carers-allowance]").getText must contain("Q1")
    }
  }

  "Does the person look after get one of required benefits" should {
    "acknowledge yes" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")
      browser.title() mustEqual "Hours - Carers Allowance"
      browser.find("div[class=carers-allowance]").getText must contain("Q2")
      browser.find("div[class=completed] ul li").getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowser {
        browser.goTo("/")
        browser.click("#q3-no")
        browser.submit("input[type='submit']")
        browser.title() mustEqual "Hours - Carers Allowance"
        browser.find("div[class=carers-allowance]").getText must contain("Q2")
        browser.find("div[class=completed] ul li").getText must contain("No")
      }
  }
}
