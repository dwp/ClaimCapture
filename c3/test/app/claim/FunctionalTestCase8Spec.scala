package app.claim

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{IterationManager, XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLClaimBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 03/09/2013
 */
class FunctionalTestCase8Spec extends FunctionalTestCommon {
  isolated

  "The application " should {

    "Successfully run absolute Test Case 8 " in new WithBrowser with G1BenefitsPageContext {

      IterationManager.init()
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase8.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title)

      lastPage match {
        case p: XmlPage => {
          val validator: XMLBusinessValidation = new XMLClaimBusinessValidation
          validateAndPrintErrors(p, claim, validator) should beTrue
        }
        case p: Page => println(p.source())
      }
    }
  } section "functional"
}