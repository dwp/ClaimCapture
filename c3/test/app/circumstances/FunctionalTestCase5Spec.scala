package app.circumstances

import utils.pageobjects.{PageObjects, XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import utils.pageobjects.circumstances.start_of_process.GReportChangesPage
import utils.{LightFakeApplication, WithJsBrowser}

class FunctionalTestCase5Spec extends FunctionalTestCommon {
  isolated

  section("functional")
  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 5" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "false"))) with PageObjects {
      val page = GReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase5.csv")
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
