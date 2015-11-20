package controllers.circs.s2_report_changes

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.{G7BreaksInCarePage,G8BreaksInCareSummaryPage}
import controllers.CircumstancesScenarioFactory


class G7BreaksInCareIntegrationSpec extends Specification {

  "Breaks in care" should {

    "be presented" in new WithBrowser with PageObjects{
      val page =  G7BreaksInCarePage(context)
      page goToThePage()
    }

    "navigate to next page when 'has this break from caring ended' yes" in new WithBrowser with PageObjects{
      val page =  G7BreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedYes
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G8BreaksInCareSummaryPage]
    }

    "navigate to next page when 'has this break from caring ended' no" in new WithBrowser with PageObjects{
      val page =  G7BreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G8BreaksInCareSummaryPage]
    }

    "navigate to next page when 'has this break from caring ended' no and expect to start caring is no" in new WithBrowser with PageObjects{
      val page =  G7BreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNoAndExpectToStartCaringNo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G8BreaksInCareSummaryPage]
    }

    "show errors when no mandatory field is filled in" in new WithBrowser with PageObjects{
      val page =  G7BreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaring
      page goToThePage()
      page fillPageWith claim

      page submitPage ()
      page.listErrors.size must beEqualTo(5)
    }

  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
