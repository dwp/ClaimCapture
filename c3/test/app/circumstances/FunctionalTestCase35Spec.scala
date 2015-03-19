package app.circumstances

import app.FunctionalTestCommon
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.{Page, XmlPage, TestData, PageObjects}
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}


class FunctionalTestCase35Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run Circumstances Test Case 35 for started and finished employment" in new WithBrowser(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {

      val page = G1ReportAChangeInYourCircumstancesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase35.csv")
      page goToThePage()

      val lastPage = page runClaimWith(circs, XmlPage.title)

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
