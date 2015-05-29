package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.{G7BreaksInCarePage,G8BreaksInCareSummaryPage}
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage


class G8BreaksInCareSummaryIntegrationSpec extends Specification with Tags {

  "Breaks in care summary" should {

    "be presented" in new WithBrowser with PageObjects{
      val page =  G8BreaksInCareSummaryPage(context)
      page goToThePage()
    }

    "navigate to next page when 'has this break from caring ended' yes and no additional breaks" in new WithBrowser with PageObjects {
      val page =  G8BreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithNoAdditionalBreaks
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

    "navigate to next page when 'has this break from caring ended' yes and no additional breaks" in new WithBrowser with PageObjects {
      val page =  G8BreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithAdditionalBreaks
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

    "show errors when additional breaks answer not specified" in new WithBrowser with PageObjects{
      val page =  G8BreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithAdditionalBreaksNotAnswered
      page goToThePage()
      page fillPageWith claim

      page submitPage ()
      page.listErrors.size must beEqualTo(1)
    }

    "show errors when additional breaks detail not specified" in new WithBrowser with PageObjects{
      val page =  G8BreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithAdditionalBreaksButNotSpecified
      page goToThePage()
      page fillPageWith claim

      page submitPage ()
      page.listErrors.size must beEqualTo(1)
    }
  } section("integration", models.domain.CircumstancesIdentification.id)
}
