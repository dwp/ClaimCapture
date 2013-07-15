package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import utils.pageobjects.s1_carers_allowance.{ApprovePage, LivingInGBPage, BenefitsPageContext, ApprovePageContext}
import utils.pageobjects.ClaimScenario

class G5ApproveIntegrationSpec extends Specification with Tags {

  "Approve" should {
    "be presented" in new WithBrowser with ApprovePageContext {
      page goToThePage()
    }
  } section "integration"

  "Carer's Allowance" should {
    "be approved" in new WithBrowser with BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      page goToThePage()
      page fillPageWith claim
      val hoursPage = page submitPage()
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage()
      over16Page fillPageWith claim
      val livingGBPage = over16Page submitPage()
      livingGBPage fillPageWith claim
      val approvePage = livingGBPage submitPage()
      approvePage match {
        case p: ApprovePage => {
          p.previousPage mustEqual Some(livingGBPage)
          p.isApproved must beTrue
        }
        case _ => false must beTrue
      }
    }

    "be declined" in new WithBrowser with BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      page fillPageWith claim
      val hoursPage = page submitPage()
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage()
      over16Page fillPageWith claim
      val livingGBPage = over16Page submitPage()
      livingGBPage fillPageWith claim
      val approvePage = livingGBPage submitPage()
      approvePage match {
        case p: ApprovePage => {
          p.previousPage mustEqual Some(livingGBPage)
          p.isNotApproved must beTrue
        }
        case _ => false must beTrue
      }
    }

    "navigate to next section" in new WithBrowser with BrowserMatchers {
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

      browser.click("#q3-no")
      browser.submit("button[type='submit']")
      titleMustEqual("Can you get Carer's Allowance?")

      browser.submit("button[type='submit']")
      titleMustEqual("Your Details - About You")
    }
  } section "integration"
}