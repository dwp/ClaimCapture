package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import utils.{WithBrowser, LightFakeApplication}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.{G1ReportChangesPage, G9EmploymentChangePage}
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage

class G9EmploymentChangeIntegrationSpec extends Specification with Tags {
  "Report a change in your circumstance - Employment" should {
    "be presented" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = G9EmploymentChangePage(context)
      page goToThePage()
    }

    "navigate to the previous page" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = G1ReportChangesPage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentNotStartedYet
      page fillPageWith(claim)
      val completedPage = page submitPage()

      val employmentChangePage = completedPage runClaimWith(claim, G9EmploymentChangePage.url)

      employmentChangePage must beAnInstanceOf[G9EmploymentChangePage]

      val prevPage = employmentChangePage.goBack()

      prevPage must beAnInstanceOf[G1ReportChangesPage]
    }

    "navigate to next page when not caring and not yet started self-employment details added" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = G9EmploymentChangePage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentNotStartedYet
      page fillPageWith(claim)

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

    "navigate to next page when caring and self-employment started details added" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = G9EmploymentChangePage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeSelfEmploymentStartedAndOngoing
      page fillPageWith(claim)

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }
  } section("integration", models.domain.CircumstancesIdentification.id)
}
