package app.circumstances

import app.FunctionalTestCommon
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage
import utils.pageobjects.xml_validation.{XMLBusinessValidation, XMLCircumstancesBusinessValidation}
import utils.pageobjects.{Page, PageObjects, TestData, XmlPage}


class FunctionalTestCase35Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 23 for Break from caring" in new WithBrowser with PageObjects {

      val page = G1ReportAChangeInYourCircumstancesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase23.csv")
      page goToThePage()

      val lastPage = page runClaimWith(circs, XmlPage.title)

      lastPage match {
        case p: XmlPage => {
          val validator: XMLBusinessValidation = new XMLCircumstancesBusinessValidation
          validateAndPrintErrors(p, circs, validator) should beTrue
        }
        case p: Page => println(p.source())
      }
    }
  } section "functional"
}
