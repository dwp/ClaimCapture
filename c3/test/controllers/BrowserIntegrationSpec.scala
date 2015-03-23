package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser
import utils.pageobjects.{Page, PageObjects, TestData}
import utils.pageobjects.s0_carers_allowance._

class BrowserIntegrationSpec extends Specification with Tags {
  "Browser" should {
    "not cache pages" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
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

      approvePageSecondTime.getClass mustEqual classOf[G6ApprovePage]
    }
  } section "integration"
}