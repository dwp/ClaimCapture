package controllers.circs.s1_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.G1AboutYouPageContext
import controllers.CircumstancesScenarioFactory


class G1AboutYouIntegrationSpec extends Specification with Tags {

  "About You" should {
    "be presented" in new WithBrowser with G1AboutYouPageContext {
      page goToThePage()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with G1AboutYouPageContext {
      page goToThePage()

      page.submitPage().listErrors.size mustEqual 6
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with G1AboutYouPageContext {
      val claim = CircumstancesScenarioFactory.aboutDetails
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

  } section("integration", models.domain.Circumstances.id)

}
