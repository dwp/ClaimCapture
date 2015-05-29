package app.circumstances

import app.FunctionalTestCommon
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_start_of_process.G1ReportChangesPage
import utils.pageobjects.xml_validation.{XMLBusinessValidation, XMLCircumstancesBusinessValidation}
import utils.pageobjects.{Page, PageObjects, TestData, XmlPage}


class FunctionalTestCase24Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 24 for Break from caring" in new WithBrowser with PageObjects {

      val page = G1ReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase24.csv")
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
