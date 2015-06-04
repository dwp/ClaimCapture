package app.circumstances

import utils.WithBrowser

import utils.pageobjects.{PageObjects, XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon

import utils.pageobjects.circumstances.s1_start_of_process.G1ReportChangesPage
import utils.pageobjects.xml_validation.{XMLBusinessValidation, XMLCircumstancesBusinessValidation}
import utils.pageobjects.{Page, PageObjects, TestData, XmlPage}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase14Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 14" in new WithBrowser with PageObjects {

      val page = G1ReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase14.csv")
      page goToThePage()

      val lastPage = page runClaimWith(circs)

      lastPage match {
        case p: XmlPage => {
          val validator: XMLBusinessValidation = new XMLCircumstancesBusinessValidation
          validateAndPrintErrors(p, circs, validator) should beTrue
        }
        case p: Page => println(p.source)
      }
    }

  } section "functional"
}