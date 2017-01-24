package controllers.circs.breaks_in_care

import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.circumstances.breaks_in_care._
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.{WithBrowser, WithJsBrowser}

class GBreaksInCareSummaryNavigationIntegrationSpec extends Specification {
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

    "navigate to hospital details page on valid submission i.e. hospital selected" in new WithJsBrowser with PageObjects {
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val claim = new TestData
      claim.BreaktypeHospitalCheckbox = "true"
      claim.BreaktypeOtherYesNo = "no"
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GCircsBreaksInCareHospitalPage]
    }

    "navigate to respite details page on valid submission i.e. respite selected" in new WithJsBrowser with PageObjects {
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val claim = new TestData
      claim.BreaktypeCareHomeCheckbox = "true"
      claim.BreaktypeOtherYesNo = "no"
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GCircsBreaksInCareRespitePage]
    }

    "navigate to other-care details page on valid submission i.e. another = yes selected" in new WithJsBrowser with PageObjects {
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val claim = new TestData
      claim.BreaktypeNoneCheckbox = "true"
      claim.BreaktypeOtherYesNo = "yes"
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GCircsBreaksInCareOtherPage]
    }

    "navigate to consent page on valid submission - no further breaks to add" in new WithJsBrowser with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2009"
      })
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val claim = new TestData
      claim.BreaktypeNoneCheckbox = "true"
      claim.BreaktypeOtherYesNo = "no"
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }

    "navigate to correct 2nd break hospital page from 3 hospital breaks when click change" in new WithJsBrowser with PageObjects {
      val numBreaks = 3
      val targetBreak = 2

      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2009"
      })
      GCircsBreaksInCareHospitalPage.fillDetails2(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_2 = "01/01/2010"
      })
      GCircsBreaksInCareHospitalPage.fillDetails3(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_3 = "01/01/2011"
      })
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
      // table rows .... 0=header 1/2=break1 3/4=break2 where 3 is the data shown and 4 is the confirm delete yes/no
      val breakDataRow = browser.find("#summary-table tr", ROWSINHEADER + (targetBreak - 1) * 2)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      // table cols ... 0=who 1=where 2=from 3=to 4=change 5=delete
      val changeLink = breakDataRow.find("td", 4).find("input")
      changeLink.getAttribute("value") mustEqual ("Change")
      changeLink.click()

      browser.url must contain(GCircsBreaksInCareHospitalPage.url)
      val hospitalAdmittedYear = browser.find("#whenWereYouAdmitted_year").getValue
      hospitalAdmittedYear mustEqual "2010"
    }

    "navigate to correct 2nd break respite page when click change" in new WithJsBrowser with PageObjects {
      val numBreaks = 3
      val targetBreak = 2

      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2000"
      })
      GCircsBreaksInCareRespitePage.fillDetails2(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_2 = "01/01/2001"
      })
      GCircsBreaksInCareHospitalPage.fillDetails3(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_3 = "01/01/2002"
      })
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
      // table rows .... 0=header 1/2=break1 3/4=break2 where 3 is the data shown and 4 is the confirm delete yes/no
      val breakDataRow = browser.find("#summary-table tr", ROWSINHEADER + (targetBreak - 1) * 2)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      // table cols ... 0=who 1=where 2=from 3=to 4=change 5=delete
      val changeLink = breakDataRow.find("td", 4).find("input")
      changeLink.getAttribute("value") mustEqual ("Change")
      changeLink.click()

      browser.url must contain(GCircsBreaksInCareRespitePage.url)
      val hospitalAdmittedYear = browser.find("#whenWereYouAdmitted_year").getValue
      hospitalAdmittedYear mustEqual "2001"
    }

    "navigate to other page which is 3rd is breaks list when click change" in new WithJsBrowser with PageObjects {
      val numBreaks = 3
      val targetBreak = 3

      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2003"
      })
      GCircsBreaksInCareRespitePage.fillDetails2(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_2 = "01/01/2004"
      })
      GCircsBreaksInCareOtherPage.fillDetails3(context, testData => {
        testData.AboutTheCareYouProvideBreakEndDate_3 = "01/01/2005"
      })
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
      // table rows .... 0=header 1/2=break1 3/4=break2 where 3 is the data shown and 4 is the confirm delete yes/no
      val breakDataRow = browser.find("#summary-table tr", ROWSINHEADER + (targetBreak - 1) * 2)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      // table cols ... 0=who 1=where 2=from 3=to 4=change 5=delete
      val changeLink = breakDataRow.find("td", 4).find("input")
      changeLink.getAttribute("value") mustEqual ("Change")
      changeLink.click()

      browser.url must contain(GCircsBreaksInCareOtherPage.url)
      val hospitalAdmittedYear = browser.find("#caringEnded_date_year").getValue
      hospitalAdmittedYear mustEqual "2005"
    }

    "delete hospital 2nd break from 3 breaks when click delete and confirm yes" in new WithJsBrowser with PageObjects {
      val numBreaks = 3
      val breakToDelete = 2

      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2000"
      })
      GCircsBreaksInCareRespitePage.fillDetails2(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_2 = "01/01/2001"
      })
      GCircsBreaksInCareHospitalPage.fillDetails3(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_3 = "01/01/2002"
      })
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
      // table rows ... 0=header 1/2=break1 3/4=break2 where 3 is the data shown and 4 is the confirm delete yes/no
      val breakDataRow = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      // table cols ... 0=who 1=where 2=from 3=to 4=change 5=delete
      val deleteLink = breakDataRow.find("td", 5).find("input")
      val confirmNoLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2 + 1).find("td", 0).find("input", 0)
      val confirmYesLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2 + 1).find("td", 0).find("input", 1)
      deleteLink.getAttribute("value") mustEqual ("Delete")
      confirmNoLink.getAttribute("value") mustEqual ("No")
      confirmYesLink.getAttribute("value") mustEqual ("Yes")
      confirmNoLink.isDisplayed mustEqual (false)
      confirmYesLink.isDisplayed mustEqual (false)
      deleteLink.click()
      confirmNoLink.isDisplayed mustEqual (true)
      confirmYesLink.isDisplayed mustEqual (true)
      confirmYesLink.click()

      // Check that returned to summary page with 2 rows and they are row1 and row3
      browser.url must contain(GCircsBreaksInCareSummaryPage.url)
      browser.find("#summary-table tr").size() mustEqual ROWSINHEADER + (numBreaks - 1) * ROWSPERBREAK
      // table cols ... 0=who 1=where 2=from 3=to 4=change 5=delete
      val fromDate1 = browser.find("#summary-table tr", 1).find("td", 2).getText
      val fromDate2 = browser.find("#summary-table tr", 3).find("td", 2).getText
      fromDate1 mustEqual "01 January 2000"
      fromDate2 mustEqual "01 January 2002"
    }

    "delete respite 1st break from 3 breaks when click delete and confirm yes" in new WithJsBrowser with PageObjects {
      val numBreaks = 3
      val breakToDelete = 1

      GCircsBreaksInCareRespitePage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2004"
      })
      GCircsBreaksInCareRespitePage.fillDetails2(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_2 = "01/01/2005"
      })
      GCircsBreaksInCareHospitalPage.fillDetails3(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_3 = "01/01/2006"
      })

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
      // table rows ... 0=header 1/2=break1 3/4=break2 where 3 is the data shown and 4 is the confirm delete yes/no
      val breakDataRow = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2)
      breakDataRow.find("td").size() mustEqual COLSINBREAKROW
      // table cols ... 0=who 1=where 2=from 3=to 4=change 5=delete
      val deleteLink = breakDataRow.find("td", 5).find("input")
      val confirmNoLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2 + 1).find("td", 0).find("input", 0)
      val confirmYesLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2 + 1).find("td", 0).find("input", 1)
      deleteLink.getAttribute("value") mustEqual ("Delete")
      confirmNoLink.getAttribute("value") mustEqual ("No")
      confirmYesLink.getAttribute("value") mustEqual ("Yes")
      confirmNoLink.isDisplayed mustEqual (false)
      confirmYesLink.isDisplayed mustEqual (false)
      deleteLink.click()
      confirmNoLink.isDisplayed mustEqual (true)
      confirmYesLink.isDisplayed mustEqual (true)
      confirmYesLink.click()

      // Check that returned to summary page with 2 rows and they are row1 and row3
      browser.url must contain(GCircsBreaksInCareSummaryPage.url)
      browser.find("#summary-table tr").size() mustEqual ROWSINHEADER + (numBreaks - 1) * ROWSPERBREAK
      // table cols ... 0=who 1=where 2=from 3=to 4=change 5=delete
      val fromDate1 = browser.find("#summary-table tr", 1).find("td", 2).getText
      val fromDate2 = browser.find("#summary-table tr", 3).find("td", 2).getText
      fromDate1 mustEqual "01 January 2005"
      fromDate2 mustEqual "01 January 2006"
    }

    "delete other break from single break when click delete" in new WithJsBrowser with PageObjects {
      val numBreaks = 1
      val breakToDelete = 1

      GCircsBreaksInCareOtherPage.fillDetails(context, testData => {})

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      browser.find("#summary-table tr").size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
      val deleteLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2).find("td", 5).find("input")
      val confirmYesLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2 + 1).find("td", 0).find("input", 1)
      confirmYesLink.isDisplayed mustEqual (false)
      deleteLink.click()
      confirmYesLink.isDisplayed mustEqual (true)
      confirmYesLink.click()

      // Check that returned to summary page with no summary table
      browser.url must contain(GCircsBreaksInCareSummaryPage.url)
      browser.find("#summary-table").size() mustEqual 0
    }

    "clicking delete break then selecting no does not delete break" in new WithJsBrowser with PageObjects {
      val numBreaks = 2
      val breakToDelete = 2

      GCircsBreaksInCareRespitePage.fillDetails(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2005"
      })
      GCircsBreaksInCareRespitePage.fillDetails2(context, testData => {
        testData.AboutTheCareYouProvideBreakWhenWereYouAdmitted_2 = "01/01/2006"
      })

      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()

      browser.find("#summary-table tr").size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
      val deleteLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2).find("td", 5).find("input")
      val confirmNoLink = browser.find("#summary-table tr", ROWSINHEADER + (breakToDelete - 1) * 2 + 1).find("td", 0).find("input", 0)
      deleteLink.getAttribute("value") mustEqual ("Delete")
      confirmNoLink.getAttribute("value") mustEqual ("No")
      confirmNoLink.isDisplayed mustEqual (false)
      deleteLink.click()
      confirmNoLink.isDisplayed mustEqual (true)
      confirmNoLink.click()

      // Check that stayed on summary page with 2 rows and confirm button is hidden again
      browser.url must contain(GCircsBreaksInCareSummaryPage.url)
      confirmNoLink.isDisplayed mustEqual (false)

      browser.find("#summary-table tr").size() mustEqual ROWSINHEADER + numBreaks * ROWSPERBREAK
    }
  }
  section("integration", models.domain.CircsBreaks.id)
}
