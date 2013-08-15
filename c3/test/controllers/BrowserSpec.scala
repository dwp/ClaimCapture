package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s1_carers_allowance.G2HoursPage

class BrowserSpec extends Specification with Tags {
  "Browser" should {
    "not cache pages" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
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
//      val claimDifferentAnswer = new ClaimScenario  <-- Jorge commented out this line
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "no"

      backToS1G1 fillPageWith claim
      val s1g2SecondTime = backToS1G1 submitPage()

      /*
      TODO
      This example is not working.
      The following assertion is not matched correctly.
      I added an alternative to this "match" in the code commented out below.
      That commented out code throws an error, which indicates that the following match is incorrect.
      I put in a "println" in the code below and it is (incorrectly) executed.
      It appears that the "goBack" used above 4 times does not work as expected.
       */
      s1g2SecondTime match {
        case p: G2HoursPage => {
          p numberSectionsCompleted() mustEqual 1
          val completed = p.findTarget("div[class=completed] ul li")
          completed(0) must contain("Q1")
          completed(0) must contain("No")
        }
        case _ => {
          println(" INCORRECTLY END UP HERE ")
          ko("Next Page is not of the right type.")
        }
      }

      /*s1g2SecondTime should beLike { case p: G2HoursPage =>
        p numberSectionsCompleted() mustEqual 1
        val completed = p.findTarget("div[class=completed] ul li")
        completed(0) must contain("Q1")
        completed(0) must contain("No")
      }*/

    }
  } section "integration"
}