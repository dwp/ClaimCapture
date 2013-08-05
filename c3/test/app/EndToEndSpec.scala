package app


import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{TestPage, PageObjectException, ClaimScenario}
import utils.pageobjects.s8_other_money.G7OtherEEAStateOrSwitzerlandPage

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 * Date: 02/08/2013
 */
class EndToEndSpec extends Specification with Tags {

  "The application " should  {
    "Successfully run absolute Happy Path, no navigation or dynamic questions triggered. Essentially mandatory only." in new WithBrowser with G1BenefitsPageContext {
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      try {
        val lastPage = page runClaimWith(claim, TestPage.title)
//        lastPage fillPageWith claim
        println(lastPage.source)
      }
      catch {
        case e:PageObjectException =>  {
          println(browser.pageSource())
          println(browser.url())
          println(e.message)
        }
      }
    }
  } section "functional"

}
