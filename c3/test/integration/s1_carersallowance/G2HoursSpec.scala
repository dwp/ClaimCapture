package integration.s1_carersallowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G2HoursSpec extends Specification with Tags {

  "Hours" should {
    "be presented" in new WithBrowser {
      browser.goTo("/allowance/hours")
      browser.title() mustEqual "Hours - Carer's Allowance"
      browser.find("div[class=carers-allowance]").getText must contain("Q2")
    }
  } section ("integration")
}