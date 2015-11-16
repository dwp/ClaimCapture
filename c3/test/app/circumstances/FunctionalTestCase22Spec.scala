package app.circumstances

import app.FunctionalTestCommon
import utils.WithJsBrowser
import utils.pageobjects.{Page, XmlPage, TestData, PageObjects}
import utils.pageobjects.circumstances.s1_start_of_process.G1ReportChangesPage
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}


class FunctionalTestCase22Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 22 for Break from caring" in new WithJsBrowser with PageObjects {

      val page = G1ReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase22.csv")
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
