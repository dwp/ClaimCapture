package app.circumstances

import utils.WithJsBrowser
import app.FunctionalTestCommon
import utils.pageobjects.circumstances.start_of_process.GReportChangesPage
import utils.pageobjects.xml_validation.{XMLBusinessValidation, XMLCircumstancesBusinessValidation}
import utils.pageobjects.{Page, PageObjects, TestData, XmlPage}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase18Spec extends FunctionalTestCommon {
  isolated

  section("functional")
  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 18" in new WithJsBrowser with PageObjects {
      val page = GReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase18.csv")
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
