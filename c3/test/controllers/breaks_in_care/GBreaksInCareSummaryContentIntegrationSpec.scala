package controllers.breaks_in_care

import controllers.WithBrowserHelper
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.{GBreaksInCareOtherPage, GBreaksInCareRespitePage, GBreaksInCareHospitalPage, GBreaksInCareSummaryPage}
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.{WithBrowser, WithJsBrowser}

class GBreaksInCareSummaryContentIntegrationSpec extends Specification {
  // 3 rows in breaks summary table for single break ... 1) header 2) data row 3) hidden delete confirm prompt
  // 6 columns in breaks data row Who/Where/From/To/Change-link/Delete-link
  val ROWSINHEADER = 1
  val ROWSPERBREAK = 2
  val COLSINBREAKROW = 6

  section("integration", models.domain.Breaks.id)
  "Breaks in care summary page" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GBreaksInCareSummaryPage]
    }

    "be presented with correct dynamic type-checkbox and other-yesno question labels" in new WithBrowser with PageObjects with WithBrowserHelper {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      $("#breaktype_questionLabel").getText() mustEqual ("Since 1 October 2016 have there been any times you or Albert Johnson have been in hospital, respite or care home for at least a week?")
      $("#breaktype_other_questionLabel").getText() mustEqual ("Have there been any other times you've not provided care for Albert Johnson for 35 hours a week?")
    }

    "be presented with correct checkbox options" in new WithBrowser with PageObjects with WithBrowserHelper {
      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()

      $("[for=breaktype_hospital]").find("span").getText() shouldEqual "Hospital"
      $("[for=breaktype_carehome]").find("span").getText() shouldEqual "Respite or care home"
      $("[for=breaktype_none]").find("span").getText() shouldEqual "None"
    }

    "present 2 errors with correct dynamic wording if no fields are populated and no existing breaks" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      val errors = page.submitPage().listErrors
      errors.size mustEqual 2
      errors(0) mustEqual ("Since 1 October 2016 have there been any times you or Albert Johnson have been in hospital, respite or care home for at least a week? - You must select 'None' if only 'None' applies.")
      errors(1) mustEqual ("Have there been any other times you've not provided care for Albert Johnson for 35 hours a week? - You must complete this section")
    }

    "present 2 errors with correct dynamic wording if no fields are populated and got existing breaks" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      val errors = page.submitPage().listErrors
      errors.size mustEqual 2
      errors(0) mustEqual ("Since 1 October 2016 have there been any other times you or Albert Johnson have been in hospital, respite or care home for at least a week? - You must select 'None' if only 'None' applies.")
      errors(1) mustEqual ("Have there been any other times you've not provided care for Albert Johnson for 35 hours a week? - You must complete this section")
    }

    "present YesNo-Other error with correct wording when submit after select Hospital checkbox but not selected YesNo-got-other-times option" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_hospital")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Have there been any other times you've not provided care for Albert Johnson for 35 hours a week? - You must complete this section")
    }

    "present YesNo-Other error with correct wording when submit after select Care Home checkbox but not selected YesNo-got-other-times option" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_carehome")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Have there been any other times you've not provided care for Albert Johnson for 35 hours a week? - You must complete this section")
    }

    "present YesNo-Other error with correct wording when submit after select None checkbox but not selected YesNo-got-other-times option" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareHospitalPage.fillDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_none")
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Have there been any other times you've not provided care for Albert Johnson for 35 hours a week? - You must complete this section")
    }

    "present Too-Many-Options error with correct wording when submit after select Hospital and None" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_hospital")
      browser.click("#breaktype_none")
      browser.click("#breaktype_other_no")
      val errors = page.submitPage().listErrors
      // We get the errors on multiple lines because we have a bullet list to explain the complex rules
      val errorWithBulletPoints = 5
      errors.size mustEqual errorWithBulletPoints
      errors(0) must contain("Since 1 October 2016 have there been any times you or Albert Johnson have been in hospital, respite or care home for at least a week? - You must select:")
      errors(1) mustEqual ("Hospital")
      errors(2) mustEqual ("Respite or care home")
      errors(3) mustEqual ("Both 'Hospital' and 'Respite or care home'")
      errors(4) mustEqual ("None")
    }

    "present Too-Many-Options error with correct wording when submit after select Respite and None" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      val page = GBreaksInCareSummaryPage(context)
      page goToThePage()
      browser.click("#breaktype_carehome")
      browser.click("#breaktype_none")
      browser.click("#breaktype_other_no")
      val errors = page.submitPage().listErrors
      // We get the errors on multiple lines because we have a bullet list to explain the complex rules
      val errorWithBulletPoints = 5
      errors.size mustEqual errorWithBulletPoints
      errors(0) must contain("Since 1 October 2016 have there been any times you or Albert Johnson have been in hospital, respite or care home for at least a week? - You must select:")
      errors(1) mustEqual ("Hospital")
      errors(2) mustEqual ("Respite or care home")
      errors(3) mustEqual ("Both 'Hospital' and 'Respite or care home'")
      errors(4) mustEqual ("None")
    }

    "display summary table with just hospital break added" in new WithJsBrowser with PageObjects {
      GBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2010"
      })

      val page = GBreaksInCareSummaryPage(context)
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
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareRespitePage.fillDetails(context, testData => {})
      val page = GBreaksInCareSummaryPage(context)
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
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      GBreaksInCareOtherPage.fillDetails(context, testData => {})
      val page = GBreaksInCareSummaryPage(context)
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
