package controllers.s_eligibility

import utils.WithApplication
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s_eligibility._

class GEligibilityIntegrationSpec extends Specification {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GEligibilityPage(context)
      page goToThePage ()
    }

    "contain errors on invalid submission" in new WithApplication {
      "missing mandatory field" in new WithBrowser with PageObjects{
			val page =  GEligibilityPage(context)
        val claim = new TestData
        claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 3
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  GEligibilityPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  GEligibilityPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GApprovePage]
    }

  }
  section("integration", models.domain.CarersAllowance.id)
}
