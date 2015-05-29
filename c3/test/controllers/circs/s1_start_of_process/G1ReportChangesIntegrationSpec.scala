package controllers.circs.s1_start_of_process

import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Specification, Tags}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s1_start_of_process.{G2ReportAChangeInYourCircumstancesPage, G1ReportChangesPage, G2ReportAChangeInYourCircumstancesPage$}
import utils.pageobjects.circumstances.s2_report_changes.{G4OtherChangeInfoPage, _}

class G1ReportChangesIntegrationSpec extends Specification with Tags {

   "Report a change in your circumstances" should {

     "be presented" in new WithBrowser with PageObjects{
       val page =  G1ReportChangesPage(context)
       page goToThePage()
     }

     "navigate to next page when addition info selected" in new WithBrowser with PageObjects{
       val page =  G1ReportChangesPage(context)
       val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
       page goToThePage()
       page fillPageWith claim

       val nextPage = page submitPage ()
       nextPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
     }

     "page contains JS enabled check" in new WithBrowser with PageObjects {
       val page = G1ReportChangesPage(context)
       page goToThePage()
       page.jsCheckEnabled must beTrue
     }

   } section("integration", models.domain.CircumstancesIdentification.id)

 }
