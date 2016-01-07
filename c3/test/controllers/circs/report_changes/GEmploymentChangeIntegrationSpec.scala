package controllers.circs.report_changes

import org.specs2.mutable._
import utils.{WithBrowser, LightFakeApplication}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.start_of_process.{GReportAChangeInYourCircumstancesPage, GReportChangesPage}
import utils.pageobjects.circumstances.report_changes.GEmploymentChangePage
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage

class GEmploymentChangeIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Report a change in your circumstance - Employment" should {
    "be presented" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()
    }

    "navigate to the previous page" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = GReportChangesPage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentNotStartedYet
      page fillPageWith(claim)
      val completedPage = page submitPage()

      val employmentChangePage = completedPage runClaimWith(claim, GEmploymentChangePage.url)

      employmentChangePage must beAnInstanceOf[GEmploymentChangePage]

      val prevPage = employmentChangePage.goBack()

      prevPage must beAnInstanceOf[GReportAChangeInYourCircumstancesPage]
    }

    "navigate to next page when not caring and not yet started self-employment details added" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentNotStartedYet
      page fillPageWith(claim)

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }

    "navigate to next page when caring and self-employment started details added" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = GEmploymentChangePage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentStartedAndOngoing
      page fillPageWith(claim)

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
