package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.G2YourContactDetailsPageContext
import controllers.CircumstancesScenarioFactory


class G2YourContactDetailsIntegrationSpec extends Specification with Tags {

  "Your contact details" should {
    "be presented" in new WithBrowser with G2YourContactDetailsPageContext {
      page goToThePage()
    }


    "present errors if mandatory fields are not populated" in new WithBrowser with G2YourContactDetailsPageContext {
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 1
    }


    "Accept submit if all mandatory fields are populated" in new WithBrowser with G2YourContactDetailsPageContext {
      val claim = CircumstancesScenarioFactory.yourContactDetails
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

  } section("integration", models.domain.CircumstancesYourContactDetails.id)

}
