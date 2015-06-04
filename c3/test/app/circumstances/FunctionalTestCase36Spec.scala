package app.circumstances

import app.FunctionalTestCommon
import play.api.test.FakeApplication
import utils.LightFakeApplication
import utils.pageobjects.{Page, XmlPage, TestData, PageObjects}
import utils.pageobjects.circumstances.s1_start_of_process.G1ReportChangesPage
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}
import utils.WithBrowser

class FunctionalTestCase36Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run Circumstances Test Case 36 for not started employment" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {

      val page = G1ReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase36.csv")
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

