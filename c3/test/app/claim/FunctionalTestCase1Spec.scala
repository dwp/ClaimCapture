package app.claim

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLClaimBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {
    "Successfully run absolute Claim Test Case 1" in new WithBrowser with G1BenefitsPageContext {

      val cofc = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      val lastPage = page runClaimWith(cofc, XmlPage.title)

      lastPage match {
        case p: XmlPage => {
          val validator: XMLBusinessValidation = new XMLClaimBusinessValidation
          validateAndPrintErrors(p, cofc, validator) should beTrue
        }
        case p: Page => println(p.source())
      }
    }

  } section "functional"
}