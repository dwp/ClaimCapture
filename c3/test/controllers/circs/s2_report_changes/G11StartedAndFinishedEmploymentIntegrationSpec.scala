package controllers.circs.s2_report_changes

import org.specs2.mutable._
import play.api.test.FakeApplication
import utils.{WithBrowser, LightFakeApplication}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.G11StartedAndFinishedEmploymentPage

class G11StartedAndFinishedEmploymentIntegrationSpec extends Specification {
  "Report an Employment change in your circumstances where the employment is finished - Employment" should {
    "be presented" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      pending("throws JavaScript error because test browser is not capable of handling the level of JS used")
      val page = G11StartedAndFinishedEmploymentPage(context)
      page goToThePage()
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
