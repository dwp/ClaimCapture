package controllers.s_eligibility

import utils.{WithJsBrowser, WithApplication, WithBrowser}
import org.specs2.mutable._
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s_eligibility._

class GEligibilityIntegrationSpec extends Specification {
  section("integration", models.domain.CarersAllowance.id)
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GEligibilityPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithApplication {
      "missing mandatory field" in new WithBrowser with PageObjects {
        val page = GEligibilityPage(context)
        val claim = new TestData
        claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 3
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects {
      val page = GEligibilityPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceWhichCountryLivein = "GB"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val page = GEligibilityPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceWhichCountryLivein = "GB"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GApprovePage]
    }

    "display origin error page if wrong country selected i.e. GB Site and NI selected" in new WithJsBrowser with PageObjects {
      val page = GEligibilityPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceWhichCountryLivein = "NI"
      page goToThePage()
      page fillPageWith claim

      page visible("#originWarning") must beFalse

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GEligibilityPage]
      nextPage.source must contain("You can't apply using this service")
      nextPage.source must contain("you must use the service on Northern Ireland Direct")
    }

    "display warning on current page if Another country is selected but allow progress to next page" in new WithJsBrowser with PageObjects {
      val page = GEligibilityPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      claim.CanYouGetCarersAllowanceWhichCountryLivein = "OTHER"
      page goToThePage()
      page fillPageWith claim

      page visible("#originWarning") must beTrue

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GApprovePage]
    }
  }
  section("integration", models.domain.CarersAllowance.id)
}
