package controllers.circs.s1_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.G3DetailsOfThePersonYouCareForPageContext
import controllers.CircumstancesScenarioFactory


class G3DetailsOfThePersonYouCareForIntegrationSpec extends Specification with Tags {

  "DetailsOfThePersonYouCareFor - Integration" should {
    "be presented" in new WithBrowser with G3DetailsOfThePersonYouCareForPageContext {
      page goToThePage()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with G3DetailsOfThePersonYouCareForPageContext {
      page goToThePage()

      page.submitPage().listErrors.size mustEqual 6
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with G3DetailsOfThePersonYouCareForPageContext {
      val claim = CircumstancesScenarioFactory.detailsOfThePersonYouCareFor
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

  } section("integration", models.domain.Circumstances.id)

}
