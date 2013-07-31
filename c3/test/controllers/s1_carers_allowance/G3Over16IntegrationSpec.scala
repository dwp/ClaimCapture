package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.{G3Over16PageContext, G3Over16Page, G2HoursPage, G1BenefitsPageContext}
import utils.pageobjects.ClaimScenario

class G3Over16IntegrationSpec extends Specification with Tags {

  "Over 16" should {
    "be presented" in new WithBrowser with G3Over16PageContext {
      page goToThePage()
    }
  } section "integration"

  "Are you aged 16 or over" should {
    "acknowledge yes" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      page goToThePage()
      page fillPageWith claim
      val hoursPage = page submitPage(waitForPage = true, waitDuration=60)
      hoursPage must beAnInstanceOf[G2HoursPage]
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage(waitForPage = true, waitDuration=60)
      over16Page must beAnInstanceOf[G3Over16Page]
      over16Page fillPageWith claim
      over16Page submitPage(waitForPage = true, waitDuration=60)
      browser.find("div[class=completed] ul li").get(2).getText must contain("Q3")
      browser.find("div[class=completed] ul li").get(2).getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "No"
      page goToThePage()
      page fillPageWith (claim)
      val hoursPage = page submitPage(waitForPage = true, waitDuration=60)
      hoursPage fillPageWith (claim)
      val over16Page = hoursPage submitPage(waitForPage = true, waitDuration=60)
      over16Page fillPageWith (claim)
      over16Page submitPage(waitForPage = true, waitDuration=60)
      browser.find("div[class=completed] ul li").get(2).getText must contain("Q3")
      browser.find("div[class=completed] ul li").get(2).getText must contain("No")
    }
  } section "integration"
}