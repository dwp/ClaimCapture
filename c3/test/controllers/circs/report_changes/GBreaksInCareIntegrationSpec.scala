package controllers.circs.report_changes

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.{GBreaksInCarePage,GBreaksInCareSummaryPage}
import controllers.CircumstancesScenarioFactory


class GBreaksInCareIntegrationSpec extends Specification {

  section("integration", models.domain.CircumstancesIdentification.id)
  "Breaks in care" should {
    "be presented" in new WithBrowser with PageObjects{
      val page =  GBreaksInCarePage(context)
      page goToThePage()
    }

    "navigate to next page when 'has this break from caring ended' yes" in new WithBrowser with PageObjects{
      val page =  GBreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedYes
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GBreaksInCareSummaryPage]
    }

    "navigate to next page when 'has this break from caring ended' no" in new WithBrowser with PageObjects{
      val page =  GBreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GBreaksInCareSummaryPage]
    }

    "navigate to next page when 'has this break from caring ended' no and expect to start caring is no" in new WithBrowser with PageObjects{
      val page =  GBreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNoAndExpectToStartCaringNo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GBreaksInCareSummaryPage]
    }

    "show errors when no mandatory field is filled in" in new WithBrowser with PageObjects{
      val page =  GBreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaring
      page goToThePage()
      page fillPageWith claim

      page submitPage ()
      page.listErrors.size must beEqualTo(5)
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
