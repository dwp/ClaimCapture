package controllers.s_self_employment

import org.specs2.mutable._
import utils.{WithApplication, WithBrowser}
import utils.pageobjects.s_self_employment.{GAboutSelfEmploymentPage, GSelfEmploymentYourAccountsPage}
import utils.pageobjects.{PageObjects,TestData}
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.s_other_money.GAboutOtherMoneyPage
import utils.pageobjects.s_employment.GEmploymentPage
import utils.pageobjects.s_claim_date.GClaimDatePageContext

class GAboutSelfEmploymentIntegrationSpec extends Specification {

  "About Self-Employment" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GAboutSelfEmploymentPage(context)
      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with GClaimDatePageContext {
      val claim = ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCareWithNoEducationAndNotEmployed()
      page goToThePage()

      val contactDetailsPage = page runClaimWith(claim, GEmploymentPage.url, waitForPage = true, trace = true)
      contactDetailsPage fillPageWith(claim)

      val nextPage = contactDetailsPage submitPage()
      nextPage must beAnInstanceOf[GAboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in new WithApplication {
      "missing mandatory fields" in new WithBrowser with PageObjects{
			val page =  GAboutSelfEmploymentPage(context)
        val claim = new TestData
        page goToThePage()
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 3
        pageWithErrors.listErrors(0) must contain("You must complete this section")
        pageWithErrors.listErrors(1) must contain("You must complete this section")
        pageWithErrors.listErrors(2) must contain("You must complete this section")
      }

      "self-employed now but missing date" in new WithBrowser with PageObjects{
			val page =  GAboutSelfEmploymentPage(context)
        val claim = new TestData
        claim.SelfEmployedAreYouSelfEmployedNow = "no"
        claim.SelfEmployedWhenDidYouStartThisJob = "11/09/2001"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0) must contain("You must complete this section")
      }

      "self-employed now but invalid date" in new WithBrowser with PageObjects{
			val page =  GAboutSelfEmploymentPage(context)
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
			val page =  GAboutSelfEmploymentPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  GAboutSelfEmploymentPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must (beAnInstanceOf[GSelfEmploymentYourAccountsPage])
    }
  }
  section("integration", models.domain.SelfEmployment.id)
}
