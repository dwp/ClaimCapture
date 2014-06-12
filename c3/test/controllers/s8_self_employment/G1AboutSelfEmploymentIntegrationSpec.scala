package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment.{G1AboutSelfEmploymentPage, G1AboutSelfEmploymentPageContext}
import utils.pageobjects.{PageObjects, IterationManager, TestData}
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s7_employment.G1EmploymentPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePageContext

class G1AboutSelfEmploymentIntegrationSpec extends Specification with Tags {

  "About Self Employment" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G1ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCareWithNoEducationAndNotEmployed()
      page goToThePage()

      val employmentHistoryPage = page runClaimWith(claim, G1EmploymentPage.title, waitForPage = true)
      employmentHistoryPage fillPageWith(claim)

      val nextPage = employmentHistoryPage submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
        val claim = new TestData
        page goToThePage()
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 2
        pageWithErrors.listErrors(0) must contain("This field is required")
      }

      "self employed now but missing date" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
        val claim = new TestData
        claim.SelfEmployedAreYouSelfEmployedNow = "no"
        claim.SelfEmployedWhenDidYouStartThisJob = "11/09/2001"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0) must contain("This field is required")
      }

      "self employed now but invalid date" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
        val claim = new TestData
        claim.SelfEmployedAreYouSelfEmployedNow = "yes"
        claim.SelfEmployedWhenDidYouStartThisJob = "01/01/0000"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0) must contain("Invalid value")
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G1AboutSelfEmploymentPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}