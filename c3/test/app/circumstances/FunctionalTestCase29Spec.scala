package app.circumstances

import play.api.test.{FakeApplication, WithBrowser}

import utils.pageobjects.{PageObjects, XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage

class FunctionalTestCase29Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 29" in new WithBrowser(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with PageObjects {
      val page = G1ReportAChangeInYourCircumstancesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase29.csv")
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