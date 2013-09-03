package controllers.circs.s1_identification

import play.api.test.WithBrowser
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.circumstances.s1_about_you._
import controllers.CircumstancesScenarioFactory

class G4CompletedIntegrationSpec extends Specification with Tags {

  "contain the completed forms" in new WithBrowser with G3DetailsOfThePersonYouCareForPageContext {
    page goToThePage()

    val claim = CircumstancesScenarioFactory.detailsOfThePersonYouCareFor
    page fillPageWith(claim)
    val completedPage = page submitPage()

    completedPage.listCompletedForms.size mustEqual 1
  }

}
