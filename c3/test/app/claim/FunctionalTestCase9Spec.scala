package app.claim

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, TestData, Page}
import utils.pageobjects.xml_validation.{XMLClaimBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 03/09/2013
 */
class FunctionalTestCase9Spec extends FunctionalTestCommon {
  isolated

  "The application " should {

    val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase9.csv")
    "Successfully run absolute Test Case 9 " in new WithBrowser with G1BenefitsPageContext {

      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title)

      lastPage match {
        case p: XmlPage => {
          val validator: XMLBusinessValidation = new XMLClaimBusinessValidation
          validateAndPrintErrors(p, claim, validator) should beTrue
        }
        case p: Page => println(p.source()); failure("bad")
      }
    }

    "validate Claim Test Case 9 with schema" in new WithBrowser with G1BenefitsPageContext {

      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title)

      lastPage match {
        case p: XmlPage => {
          val validator = XmlValidatorFactory.buildCaValidator()
          validator.validate(p.source()) should beTrue
        }
        case p: Page => println(p.source()); failure("bad")
      }
    }
  } section "functional"
}