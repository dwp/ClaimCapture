package controllers.breaks_in_care

import controllers.WithBrowserHelper
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.{GBreaksInCareOtherPage, GBreaksInCareRespitePage, GBreaksInCareHospitalPage, GBreaksInCareSummaryPage}
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.{WithBrowser, WithJsBrowser}

class GBreaksInCareSummaryContentIntegrationSpec extends Specification {
  section("integration", models.domain.YourIncomes.id)
  "Breaks in care summary page" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GBreaksInCareSummaryPage]
    }

    "be presented with correct dynamic question labels" in new WithBrowser with PageObjects with WithBrowserHelper {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      $("#breaksummary_other_questionLabel").getText() mustEqual ("Since 1 October 2016, were there any times you or Albert Johnson have been in hospital, respite or a care home for at least a week or where you've not provided care for 35 hours a week?")
    }

    "be presented with correct radio options when select yes and no existing breaks" in new WithBrowser with PageObjects with WithBrowserHelper {
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()

      // A time when you have not provided care for 35 hours a week
      $("#breaksummary_answer").find("span", 0).getText() shouldEqual "Hospital admission"
      $("#breaksummary_answer").find("span", 1).getText() shouldEqual "Respite or care home admission"
      $("#breaksummary_answer").find("span", 2).getText() shouldEqual "A time when you have not provided care for 35 hours a week"
    }

    "be presented with correct radio options when select yes and got existing breaks" in new WithBrowser with PageObjects with WithBrowserHelper {
      GBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()

      // Another time when you have not provided care for 35 hours a week
      $("#breaksummary_answer").find("span", 0).getText() shouldEqual "Hospital admission"
      $("#breaksummary_answer").find("span", 1).getText() shouldEqual "Respite or care home admission"
      $("#breaksummary_answer").find("span", 2).getText() shouldEqual "Another time when you have not provided care for 35 hours a week"
    }

    "present 1 error with correct wording if no fields are populated and no existing breaks" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Since 1 October 2016, were there any times you or Albert Johnson have been in hospital, respite or a care home for at least a week or where you've not provided care for 35 hours a week? - You must complete this section")
    }

    "present 1 error with correct wording if no fields are populated and got existing hospital break" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Since 1 October 2016, were there any other times you or Albert Johnson have been in hospital, respite or a care home for at least a week or where you've not provided care for 35 hours a week? - You must complete this section")
    }

    "present 1 error with correct wording when select Yes-got-other-times but not selected break-type radio option" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaksummary_other_yes")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("What do you want to add? - You must complete this section")
    }

    // 3 rows in breaks summary table for single break ... 1) header 2) data row 3) hidden delete confirm prompt
    // 6 columns in breaks data row Who/Where/From/To/Change-link/Delete-link
    val ROWSINHEADER=1
    val ROWSPERBREAK=2
    val COLSINBREAKROW=6

    "display summary table with just hospital break added" in new WithJsBrowser with PageObjects {
      GBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2010"
      })

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows=browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER+ROWSPERBREAK
      val breakDataRow=browser.find("#summary-table tr", 1)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      breakDataRow.find("td",0).getText() mustEqual("You")
      breakDataRow.find("td",1).getText() mustEqual("Hospital")
      breakDataRow.find("td",2).getText() mustEqual("01/01/2010")
      breakDataRow.find("td",3).getText() mustEqual("Not ended")
      breakDataRow.find("td",4).find("input").getAttribute("value") mustEqual( "Change")
      breakDataRow.find("td",5).find("input").getAttribute("value") mustEqual( "Delete")
    }

    "display summary table with just respite break added" in new WithJsBrowser with PageObjects {
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareRespitePage.fillDetails(context, testData => {})
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows=browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER+ROWSPERBREAK
      val breakDataRow=browser.find("#summary-table tr", 1)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      breakDataRow.find("td",0).getText() mustEqual("You")
      breakDataRow.find("td",1).getText() mustEqual("Respite or care home")
      breakDataRow.find("td",2).getText() mustEqual("01/10/2015")
      breakDataRow.find("td",3).getText() mustEqual("Not ended")
      breakDataRow.find("td",4).find("input").getAttribute("value") mustEqual( "Change")
      breakDataRow.find("td",5).find("input").getAttribute("value") mustEqual( "Delete")
    }

    "display summary table with just other-care break added" in new WithJsBrowser with PageObjects {
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      // WAITING ON OtherPage.fillDetails ...
/*      GBreaksInCareOtherPage.fillDetails(context, testData => {})
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows=browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER+ROWSPERBREAK
      val breakDataRow=browser.find("#summary-table tr", 1)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      breakDataRow.find("td",0).getText() mustEqual("You")
      breakDataRow.find("td",1).getText() mustEqual("Respite or care home")
      breakDataRow.find("td",2).getText() mustEqual("01/10/2015")
      breakDataRow.find("td",3).getText() mustEqual("Not ended")
      breakDataRow.find("td",4).find("input").getAttribute("value") mustEqual( "Change")
      breakDataRow.find("td",5).find("input").getAttribute("value") mustEqual( "Delete")
      */
    }
  }
  section("integration", models.domain.YourIncomes.id)
}
