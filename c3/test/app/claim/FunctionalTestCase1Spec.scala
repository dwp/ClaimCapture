package app.claim

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.{G1BenefitsPage, G1BenefitsPageContext}
import utils.pageobjects._
import utils.pageobjects.xml_validation.{XMLClaimBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import org.specs2.specification.Scope
import controllers.s1_carers_allowance.G1Benefits

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {
    "Successfully run absolute Claim Test Case 1" in new WithBrowser with PageObjects {

      val page = G1BenefitsPage(context)
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
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

  } section ("functional","claim")
}

