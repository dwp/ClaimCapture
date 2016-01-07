package controllers.circs.report_changes

import org.specs2.mutable._
import utils.{WithBrowser, LightFakeApplication}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.GStartedAndFinishedEmploymentPage

class GStartedAndFinishedEmploymentIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Report an Employment change in your circumstances where the employment is finished - Employment" should {
    "be presented" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      pending("throws JavaScript error because test browser is not capable of handling the level of JS used")
      val page = GStartedAndFinishedEmploymentPage(context)
      page goToThePage()
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
