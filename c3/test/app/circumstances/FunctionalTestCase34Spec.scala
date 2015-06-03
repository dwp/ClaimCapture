package app.circumstances

import play.api.test.FakeApplication
import utils.LightFakeApplication

import utils.pageobjects.{PageObjects, XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import utils.pageobjects.circumstances.s1_start_of_process.G1ReportChangesPage
import app.ConfigProperties._
import utils.WithBrowser

class FunctionalTestCase34Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
      "Successfully run absolute Circumstances Test Case 34" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {

        val page = G1ReportChangesPage(context)
        val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase34.csv")
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
