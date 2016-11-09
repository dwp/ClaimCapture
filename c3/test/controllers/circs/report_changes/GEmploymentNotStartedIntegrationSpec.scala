package controllers.circs.report_changes

import org.specs2.mutable._
import utils.{WithBrowser, LightFakeApplication}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.{GEmploymentPayPage}

class GEmploymentNotStartedIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Report an Employment change in your circumstances where employment has not started - Employment" should {
     "be presented" in new WithBrowser with PageObjects {
       val page = GEmploymentPayPage(context)
       page goToThePage()
     }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
