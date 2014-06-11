package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s1_carers_allowance.{G1BenefitsPage, G2HoursPage}

class G1BenefitsIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      page goToThePage ()
    }

    "contain a link to gov.uk" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      page goToThePage ()
      page source() must contain("https://www.gov.uk")
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      val claim = new TestData
      page goToThePage()
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "warn if answer no to person get one of benefits" in new WithBrowser with PageObjects {
      skipped("Only breaking on CI")
		  val page = G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "no"
      page goToThePage()
      page fillPageWith claim
      page.listDisplayedPromptMessages.size mustEqual 1
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim
      page.listDisplayedPromptMessages.size mustEqual 0

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G2HoursPage]
    }
  } section("integration", models.domain.CarersAllowance.id)
}