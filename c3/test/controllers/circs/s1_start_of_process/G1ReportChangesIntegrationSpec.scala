package controllers.circs.s1_start_of_process

import app.ReportChange._
import controllers.CircumstancesScenarioFactory
import org.openqa.selenium.By
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.circumstances.s1_start_of_process.{G2ReportAChangeInYourCircumstancesPage, G1ReportChangesPage}
import utils.pageobjects.circumstances.s2_report_changes.{G4OtherChangeInfoPage, _}
import CircumstancesScenarioFactory._

class G1ReportChangesIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesReportChanges.id)
  "Report a change in your circumstances" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G1ReportChangesPage(context)
      page goToThePage()
    }

    "navigate to next page when addition info selected" in new WithBrowser with PageObjects {
      val page = G1ReportChangesPage(context)
      val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
    }

    "page contains JS enabled check" in new WithBrowser with PageObjects {
      val page = G1ReportChangesPage(context)
      page goToThePage()
      page.jsCheckEnabled must beTrue
    }

    "Not save 2 mutually exclusive reasons - see DE944" in new WithBrowser with PageObjects {

      // selects one reason and submits it to the last page so it gets saved
      // then selects a different reason and checks that the data refering to "you" is still saved
      // then it selects the same initial reason and checks that the data input initially was cleared by the second step

      val reportChangesFirst =  G1ReportChangesPage(context)
      reportChangesFirst goToThePage()

      val otherChangeInfoPageFirst = reportChangesFirst runClaimWith (reportChangesOtherChangeInfo, G4OtherChangeInfoPage.url)
      otherChangeInfoPageFirst must beAnInstanceOf[G4OtherChangeInfoPage]
      otherChangeInfoPageFirst.fillInput("#changeInCircs", "bla bla")

      //submit to the last page and go back to the selectionPage
      val lastPage = otherChangeInfoPageFirst.submitPage()
      val reportChangesSecond = lastPage.goBack().goBack().goBack()
      reportChangesSecond must beAnInstanceOf[G1ReportChangesPage]


      //select a different Ch of circs
      val justAddress = new TestData
      justAddress.CircumstancesReportChanges = AddressChange.name
      val addressPage = reportChangesSecond runClaimWith (justAddress, G6AddressChangePage.url)
      //verifies that the second page was already filled
      addressPage must beAnInstanceOf[G6AddressChangePage]


      //select the first Ch of circs again - and verify that it's value was cleared by the previous step
      val reportChangesThird = addressPage.goBack().goBack()
      reportChangesThird must beAnInstanceOf[G1ReportChangesPage]

      val justOtherInfo = new TestData
      justOtherInfo.CircumstancesReportChanges = AdditionalInfo.name
      val otherChangeInfoPageSecond = reportChangesThird runClaimWith (justOtherInfo, G4OtherChangeInfoPage.url)
      otherChangeInfoPageSecond must beAnInstanceOf[G4OtherChangeInfoPage]

      //verify that the other field is not filled
      otherChangeInfoPageSecond.ctx.browser.getDriver.findElement(By.id("changeInCircs")).getText must_== ""
      //the submit fails with validation
      val samePage = otherChangeInfoPageSecond.submitPage()
      samePage must beAnInstanceOf[G4OtherChangeInfoPage]
    }
  }
  section("integration", models.domain.CircumstancesReportChanges.id)
}
