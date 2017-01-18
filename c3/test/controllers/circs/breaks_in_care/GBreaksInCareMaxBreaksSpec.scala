package controllers.circs.breaks_in_care

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import models.domain.Breaks
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.circumstances.breaks_in_care.{GCircsBreaksInCareOtherPage, GCircsBreaksInCareRespitePage, GCircsBreaksInCareHospitalPage, GCircsBreaksInCareSummaryPage}
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.{LightFakeApplication, WithBrowser}

class GBreaksInCareMaxBreaksSpec extends Specification {
  // 3 rows in breaks summary table for single break ... 1) header 2) data row 3) hidden delete confirm prompt
  // 6 columns in breaks data row Who/Where/From/To/Change-link/Delete-link
  val ROWSINHEADER = 1
  val ROWSPERBREAK = 2

  section("integration", models.domain.Breaks.id)
  "Breaks in care pages" should {
    "present summary page" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      println("GBreaksInCareMaxBreaksSpec max breaks set to:"+Breaks.maximumBreaks)
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareSummaryPage]
    }
/*
    "present error on summary page with max breaks and try to submit Hospital" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareSummaryPage]

      val claim = new TestData
      claim.BreaktypeHospitalCheckbox = "true"
      claim.BreaktypeOtherYesNo = "no"
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage.source must contain("Maximum breaks (3) is reached.")
    }

    "present error on summary page with max breaks and try to submit CareHome" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareSummaryPage]

      val claim = new TestData
      claim.BreaktypeCareHomeCheckbox = "true"
      claim.BreaktypeOtherYesNo = "no"
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage.source must contain("Maximum breaks (3) is reached.")
    }

    "present error on summary page with max breaks and try to submit Other=yes" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareSummaryPage]

      val claim = new TestData
      claim.BreaktypeNoneCheckbox = "true"
      claim.BreaktypeOtherYesNo = "yes"
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage.source must contain("Maximum breaks (3) is reached.")
    }

    "allow submit of summary page with max breaks and None / no selected" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareSummaryPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareSummaryPage]

      val claim = new TestData
      claim.BreaktypeNoneCheckbox = "true"
      claim.BreaktypeOtherYesNo = "no"
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GYourCourseDetailsPage]
    }

    "not add more than max breaks in Hospital page if navigate directly" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareHospitalPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareHospitalPage]

      val claim = new TestData
      claim.AboutTheCareYouProvideBreakWhoWasInHospital_1 = BreaksInCareGatherOptions.You
      claim.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2016"
      claim.AboutTheCareYouProvideYourStayEnded_1 = Mappings.no
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GCircsBreaksInCareSummaryPage]
      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + ROWSPERBREAK * 3
    }

    "not add more than max breaks in CareHome page if navigate directly" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareRespitePage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareRespitePage]

      val claim = new TestData
      claim.AboutTheCareYouProvideBreakWhoWasInRespite_1 = BreaksInCareGatherOptions.You
      claim.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/10/2015"
      claim.AboutTheCareYouProvideYourStayEnded_1 = Mappings.no
      claim.AboutTheCareYouProvideYourMedicalProfessional_1 = Mappings.no
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GCircsBreaksInCareSummaryPage]
      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + ROWSPERBREAK * 3
    }

    "not add more than max breaks in Other page if navigate directly" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("maximumBreaksInCare" -> 3))) with PageObjects {
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      GCircsBreaksInCareHospitalPage.fillDetails(context, testData => {})
      val page = GCircsBreaksInCareOtherPage(context)
      page goToThePage()
      page must beAnInstanceOf[GCircsBreaksInCareOtherPage]

      val claim = new TestData
      claim.AboutTheCareYouProvideBreakEndDate_1 = "01/10/2015"
      claim.AboutTheCareYouProvideBreakStartAnswer_1 = Mappings.no
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GCircsBreaksInCareSummaryPage]
      val summaryTableRows = browser.find("#summary-table tr")
      summaryTableRows.size() mustEqual ROWSINHEADER + ROWSPERBREAK * 3
    }
    */
  }
  section("integration", models.domain.Breaks.id)
}
