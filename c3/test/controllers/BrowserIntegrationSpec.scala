package controllers

import org.specs2.mutable._

import utils.WithBrowser
import utils.pageobjects.{Page, PageObjects, TestData}
import utils.pageobjects.s_eligibility._

class BrowserIntegrationSpec extends Specification {
  "Browser" should {
    "not cache pages" in new WithBrowser with PageObjects {
      val page = GBenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "PIP"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()

      val eligibilityPage = page fillPageWith claim submitPage()
      val approvePage = eligibilityPage fillPageWith claim submitPage()
      val disclaimerPage = approvePage fillPageWith claim submitPage()

      val benefitsPage = disclaimerPage goBack() goBack() goBack()

      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "NONE"

      benefitsPage fillPageWith claim
      val eligibilityPageSecondTime = benefitsPage submitPage()
      val approvePageSecondTime = eligibilityPageSecondTime submitPage()

      approvePageSecondTime.getClass mustEqual classOf[GApprovePage]
    }
  }
section("integration")
}
