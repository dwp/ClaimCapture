package app.circumstances

import utils.{WithJsBrowser}

import app.FunctionalTestCommon
import utils.pageobjects.circumstances.start_of_process.GReportChangesPage
import utils.pageobjects.xml_validation.{XMLBusinessValidation, XMLCircumstancesBusinessValidation}
import utils.pageobjects.{Page, PageObjects, TestData, XmlPage}

class FunctionalTestCase29Spec extends FunctionalTestCommon {
  isolated

  section("functional")
  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 29" in new WithJsBrowser with PageObjects {
      val page = GReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase29.csv")
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
  }
  section("functional")
}
