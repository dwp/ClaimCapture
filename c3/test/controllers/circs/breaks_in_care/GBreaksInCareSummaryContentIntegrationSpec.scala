package controllers.circs.breaks_in_care

import controllers.{WithBrowserHelper}
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.circumstances.breaks_in_care.{GCircsBreaksInCareHospitalPage, GCircsBreaksInCareSummaryPage, GCircsBreaksInCareOtherPage, GCircsBreaksInCareRespitePage}
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{WithBrowser, WithJsBrowser}

class GBreaksInCareSummaryContentIntegrationSpec extends Specification {
  // 3 rows in breaks summary table for single break ... 1) header 2) data row 3) hidden delete confirm prompt
  // 6 columns in breaks data row Who/Where/From/To/Change-link/Delete-link
  val ROWSINHEADER = 1
  val ROWSPERBREAK = 2
  val COLSINBREAKROW = 6

  section("integration", models.domain.Breaks.id)
  "Circs Breaks in care summary page" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareSummaryPage]
    }

    "be presented with correct dynamic type-checkbox and other-yesno question labels" in new WithBrowser with PageObjects with WithBrowserHelper {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      $("#breaktype_questionLabel").getText() mustEqual ("Have there been any times you or Mrs Jane Johnson have been in hospital, respite or care home?")
      $("#breaktype_other_questionLabel").getText() mustEqual ("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week?")
    }

    "be presented with correct checkbox options" in new WithBrowser with PageObjects with WithBrowserHelper {
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      $("[for=breaktype_hospital]").find("span").getText() shouldEqual "Hospital"
      $("[for=breaktype_carehome]").find("span").getText() shouldEqual "Respite or care home"
      $("[for=breaktype_none]").find("span").getText() shouldEqual "None"
    }

    "present correct question labels when no existing breaks" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page.source must contain("Have there been any times you or Mrs Jane Johnson have been in hospital, respite or care home?")
      page.source must contain("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week?")
    }

    "present correct question labels when got existing break" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page.source must contain("Have there been any other times you or Mrs Jane Johnson have been in hospital, respite or care home?")
      page.source must contain("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week?")
    }

    "present 2 errors with correct dynamic wording if no fields are populated and no existing break" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val errors = page.submitPage().listErrors
      // We get the errors on multiple lines because we have a bullet list to explain the complex rules
      val errorWithBulletPoints = 6
      errors.size mustEqual errorWithBulletPoints
      errors(0) must contain("Have there been any times you or Mrs Jane Johnson have been in hospital, respite or care home? - You must select:")
      errors(1) mustEqual ("Hospital")
      errors(2) mustEqual ("Respite or care home")
      errors(3) mustEqual ("Both 'Hospital' and 'Respite or care home'")
      errors(4) mustEqual ("None")
      errors(5) mustEqual ("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week? - You must complete this section")
    }

    "present 2 errors with correct dynamic wording if no fields are populated and got existing break" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val errors = page.submitPage().listErrors
      val errorWithBulletPoints = 6
      errors.size mustEqual errorWithBulletPoints
      errors(0) must contain("Have there been any other times you or Mrs Jane Johnson have been in hospital, respite or care home? - You must select:")
      errors(1) mustEqual ("Hospital")
      errors(2) mustEqual ("Respite or care home")
      errors(3) mustEqual ("Both 'Hospital' and 'Respite or care home'")
      errors(4) mustEqual ("None")
      errors(5) mustEqual ("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week? - You must complete this section")
    }

    "present YesNo-Other error with correct wording when submit after select Hospital checkbox but not selected YesNo-got-other-times option" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_hospital")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week? - You must complete this section")
    }

    "present YesNo-Other error with correct wording when submit after select Care Home checkbox but not selected YesNo-got-other-times option" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_carehome")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week? - You must complete this section")
    }

    "present YesNo-Other error with correct wording when submit after select None checkbox but not selected YesNo-got-other-times option" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_none")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Have there been any other times you've not provided care for Mrs Jane Johnson for 35 hours a week? - You must complete this section")
    }

    "present Too-Many-Options error with correct wording when submit after select Hospital and None" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_hospital")
      browser.click("#breaktype_none")
      browser.click("#breaktype_other_no")
      val errors = page.submitPage().listErrors
      // We get the errors on multiple lines because we have a bullet list to explain the complex rules
      val errorWithBulletPoints = 1
      errors.size mustEqual errorWithBulletPoints
      errors(0) mustEqual ("Have there been any times you or Mrs Jane Johnson have been in hospital, respite or care home? - You must select 'None' if only 'None' applies.")
    }

    "present Too-Many-Options error with correct wording when submit after select Respite and None" in new WithJsBrowser with PageObjects {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_carehome")
      browser.click("#breaktype_none")
      browser.click("#breaktype_other_no")
      val errors = page.submitPage().listErrors
      // We get the errors on multiple lines because we have a bullet list to explain the complex rules
      val errorWithBulletPoints = 1
      errors.size mustEqual errorWithBulletPoints
      errors(0) mustEqual ("Have there been any times you or Mrs Jane Johnson have been in hospital, respite or care home? - You must select 'None' if only 'None' applies.")
    }

    "display summary table with just hospital break added" in new WithJsBrowser with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2010"
      })
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + ROWSPERBREAK
      val breakDataRow = browser.find("#summary-table tr", 1)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      breakDataRow.find("td", 0).getText() mustEqual ("You")
      breakDataRow.find("td", 1).getText() mustEqual ("Hospital")
      breakDataRow.find("td", 2).getText() mustEqual ("01 January 2010")
      breakDataRow.find("td", 3).getText() mustEqual ("Not ended")
      breakDataRow.find("td", 4).find("input").getAttribute("value") mustEqual ("Change")
      breakDataRow.find("td", 5).find("input").getAttribute("value") mustEqual ("Delete")
    }

    "display summary table with just respite break added" in new WithJsBrowser with PageObjects {
      GCircsBreaksInCareRespitePage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + ROWSPERBREAK
      val breakDataRow = browser.find("#summary-table tr", 1)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      breakDataRow.find("td", 0).getText() mustEqual ("You")
      breakDataRow.find("td", 1).getText() mustEqual ("Respite or care home")
      breakDataRow.find("td", 2).getText() mustEqual ("01 October 2015")
      breakDataRow.find("td", 3).getText() mustEqual ("Not ended")
      breakDataRow.find("td", 4).find("input").getAttribute("value") mustEqual ("Change")
      breakDataRow.find("td", 5).find("input").getAttribute("value") mustEqual ("Delete")
    }

    "display summary table with just other-care break added" in new WithJsBrowser with PageObjects {
      GCircsBreaksInCareOtherPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + ROWSPERBREAK
      val breakDataRow = browser.find("#summary-table tr", 1)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      breakDataRow.find("td", 0).getText() mustEqual ("You")
      breakDataRow.find("td", 1).getText() mustEqual ("Other")
      breakDataRow.find("td", 2).getText() mustEqual ("01 October 2015")
      breakDataRow.find("td", 3).getText() mustEqual ("Not ended")
      breakDataRow.find("td", 4).find("input").getAttribute("value") mustEqual ("Change")
      breakDataRow.find("td", 5).find("input").getAttribute("value") mustEqual ("Delete")
    }
  }
  section("integration", models.domain.Breaks.id)
}
