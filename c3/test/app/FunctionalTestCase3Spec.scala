package app

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, TestData, Page}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase3Spec extends FunctionalTestCommon {
  isolated

  "The application " should {

    "Successfully run absolute Test Case 3 " in new WithBrowser with G1BenefitsPageContext {

      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase3.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title)

      lastPage match {
        case p: XmlPage => {
          validateAndPrintErrors(p, claim) should beTrue
        }
        case p: Page => println(p.source())
      }
    }
  } section "functional"
}