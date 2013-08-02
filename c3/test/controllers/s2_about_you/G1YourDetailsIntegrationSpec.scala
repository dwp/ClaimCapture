package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s2_about_you.G1YourDetailsPageContext
import utils.pageobjects.s1_carers_allowance.G5ApprovePage
import controllers.ClaimScenarioFactory

class G1YourDetailsIntegrationSpec extends Specification with Tags {

  "Your Details" should {
    "be presented" in new WithBrowser with G1YourDetailsPageContext {
      page goToThePage()
    }

    "navigate back to approve page" in new WithBrowser with G1YourDetailsPageContext {
      page goToThePage()
      val backPage = page goBack()
      backPage must beAnInstanceOf[G5ApprovePage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with G1YourDetailsPageContext {
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 7
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
    //  println(page.source)
      page submitPage()
    }

  } section "integration"
}