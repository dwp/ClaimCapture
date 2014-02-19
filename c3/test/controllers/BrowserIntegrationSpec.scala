package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s1_carers_allowance._

class BrowserIntegrationSpec extends Specification with Tags {
  "Browser" should {
    "not cache pages" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()

      val s1g2 = page fillPageWith claim submitPage() 
      val s1g3 = s1g2 fillPageWith claim submitPage()
      val s1g4 = s1g3 fillPageWith claim submitPage()
      val approvalPage = s1g4 fillPageWith claim submitPage()
      val backToS1G1 = approvalPage goBack() goBack() goBack() goBack()
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "no"

      backToS1G1 fillPageWith claim
      val s1g2SecondTime = backToS1G1 submitPage()

      s1g2SecondTime should beLike { case p: G2HoursPage =>
        p numberSectionsCompleted() shouldEqual 1
        val completed = p.listCompletedForms
        completed(0) must contain("Does the person you care for get one of these benefits?")
        completed(0) must contain("No")
      }
    }
  } section "integration"
}