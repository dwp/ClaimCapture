package controllers.s7_self_employment

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.s7_self_employment.{G1AboutSelfEmploymentPage, G2SelfEmploymentYourAccountsPage}
import utils.pageobjects.{PageObjects,TestData}
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s8_employment.G1EmploymentPage
import utils.pageobjects.s_claim_date.GClaimDatePageContext

class G1AboutSelfEmploymentIntegrationSpec extends Specification with Tags {

  "About Self Employment" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with GClaimDatePageContext {
      val claim = ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCareWithNoEducationAndNotEmployed()
      page goToThePage()

      val contactDetailsPage = page runClaimWith(claim, G1EmploymentPage.url, waitForPage = true, trace = true)
      contactDetailsPage fillPageWith(claim)

      val nextPage = contactDetailsPage submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory fields" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
        val claim = new TestData
        page goToThePage()
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 3
        pageWithErrors.listErrors(0) must contain("This field is required")
        pageWithErrors.listErrors(1) must contain("This field is required")
        pageWithErrors.listErrors(2) must contain("This field is required")
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
        claim.SelfEmployedNatureofYourBusiness = "Some type of business"
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

      nextPage must (beAnInstanceOf[G2SelfEmploymentYourAccountsPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}