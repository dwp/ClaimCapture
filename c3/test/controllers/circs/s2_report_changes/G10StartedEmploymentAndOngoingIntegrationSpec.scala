package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.G10StartedEmploymentAndOngoingPage

class G10StartedEmploymentAndOngoingIntegrationSpec extends Specification with Tags {
  "Report an Employment change in your circumstances where the employment is ongoing - Employment" should {
    "be presented" in new WithBrowser(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      pending("throws JavaScript error because test browser is not capable of handling the level of JS used")
      val page = G10StartedEmploymentAndOngoingPage(context)
      page goToThePage()
    }
  } section("integration", models.domain.CircumstancesIdentification.id)
}
