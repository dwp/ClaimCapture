package app.circumstances

import app.FunctionalTestCommon
import utils.LightFakeApplication
import utils.pageobjects.{Page, XmlPage, TestData, PageObjects}
import utils.pageobjects.circumstances.start_of_process.GReportChangesPage
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}
import utils.WithJsBrowser

class FunctionalTestCase35Spec extends FunctionalTestCommon {
  isolated

  section("functional")
  "The application Circumstances" should {
    "Successfully run Circumstances Test Case 35 for started and finished employment" in new WithJsBrowser with PageObjects {
      val page = GReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase35.csv")
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
