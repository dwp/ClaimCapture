package app.circumstances

import play.api.test.WithBrowser

import utils.pageobjects.{XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import utils.pageobjects.circumstances.s1_about_you.{G1AboutYouPageContext}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 1" in new WithBrowser with G1AboutYouPageContext {

      val claim = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage()

      val lastPage = page runClaimWith(claim, XmlPage.title)

      lastPage match {
        case p: XmlPage => {
          val validator: XMLBusinessValidation = new XMLCircumstancesBusinessValidation
          validateAndPrintErrors(p, claim, validator) should beTrue
        }
        case p: Page => println(p.source())
      }
    }.pendingUntilFixed("throws a 'SAXParseException: Content is not allowed in prolog' because we are not returning valid XML yet")

  } section "functional"
}