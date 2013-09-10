package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s1_carers_allowance.G6ApprovePage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s10_pay_details.G1HowWePayYouPageContext
import utils.pageobjects.S11_consent_and_declaration.G3DisclaimerPageContext
import utils.pageobjects.TestData

class G1YourDetailsIntegrationSpec extends Specification with Tags {
  "Your Details" should {
    "be presented" in new WithBrowser with G1YourDetailsPageContext {
      page goToThePage()
    }

    "navigate back to approve page" in new WithBrowser with G1YourDetailsPageContext {
      browser goTo "/allowance/approve"

      page goToThePage()
      val backPage = page goBack()
      backPage must beAnInstanceOf[G6ApprovePage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with G1YourDetailsPageContext {
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 8
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val g2 = page submitPage()
      
      g2 must beAnInstanceOf[G2ContactDetailsPage]
    }
    
    "contain error if invalid nationality containing numbers" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouNationality = "a12345"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Nationality")
    }
    
    "contain error if invalid nationality containing special characters" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouNationality = "a!@£$%^&*(){}"
      page goToThePage()
      page fillPageWith claim

      page.submitPage().listErrors.size mustEqual 1
    }
  } section("integration", models.domain.AboutYou.id)
}