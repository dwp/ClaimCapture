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

      val s1g2 = page fillPageWith claim submitPage() 
      val s1g3 = s1g2 fillPageWith claim submitPage()
      val s1g4 = s1g3 fillPageWith claim submitPage()
      val approvalPage = s1g4 fillPageWith claim submitPage()
      val backToS1G1 = approvalPage goBack() goBack()

      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "NONE"

      backToS1G1 fillPageWith claim
      val s1g2SecondTime = backToS1G1 submitPage()

      s1g2SecondTime.getClass mustEqual classOf[G6ApprovePage]
    }
  } section "integration"
}