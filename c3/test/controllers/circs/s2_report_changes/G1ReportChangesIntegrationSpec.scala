package controllers.circs.s2_report_changes

import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.circumstances.s2_report_changes._
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.G4OtherChangeInfoPage
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage

class G1ReportChangesIntegrationSpec extends Specification with Tags {

   "Report a change in your circumstances" should {

     "be presented" in new WithBrowser with PageObjects{
       val page =  G1ReportChangesPage(context)
       page goToThePage()
     }

     "navigate to previous page" in new WithBrowser with PageObjects{
       val page =  G1ReportAChangeInYourCircumstancesPage(context)
       page goToThePage()

       val claim = CircumstancesScenarioFactory.aboutDetails
       page fillPageWith(claim)
       val completedPage = page submitPage()

       val reportChangesPage = completedPage runClaimWith (claim, G1ReportChangesPage.url)

       reportChangesPage must beAnInstanceOf[G1ReportChangesPage]

       val prevPage = reportChangesPage.goBack()

       prevPage must beAnInstanceOf[G1ReportAChangeInYourCircumstancesPage]
     }

     "navigate to next page when addition info selected" in new WithBrowser with PageObjects{
       val page =  G1ReportChangesPage(context)
       val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
       page goToThePage()
       page fillPageWith claim

       val nextPage = page submitPage ()
       nextPage must beAnInstanceOf[G4OtherChangeInfoPage]
     }

     "navigate to next page when self employment selected" in new WithBrowser(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "false"))) with PageObjects{
       val page =  G1ReportChangesPage(context)
       val claim = CircumstancesScenarioFactory.reportChangesSelfEmployment
       page goToThePage()
       page fillPageWith claim

       val nextPage = page submitPage ()
       nextPage must beAnInstanceOf[G2SelfEmploymentPage]
     }

     "navigate to next page when stopped caring selected" in new WithBrowser with PageObjects{
       val page =  G1ReportChangesPage(context)
       val claim = CircumstancesScenarioFactory.reportChangesStoppedCaring
       page goToThePage()
       page fillPageWith claim

       val nextPage = page submitPage ()
       nextPage must beAnInstanceOf[G3PermanentlyStoppedCaringPage]
     }

     "navigate to next page when break from caring selected" in new WithBrowser with PageObjects{
       val page =  G1ReportChangesPage(context)
       val claim = CircumstancesScenarioFactory.reportBreakFromCaring
       page goToThePage()
       page fillPageWith claim

       val nextPage = page submitPage ()
       nextPage must beAnInstanceOf[G7BreaksInCarePage]
     }

   } section("integration", models.domain.CircumstancesIdentification.id)

 }
