package app

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{PageObjectException,XmlPage, ClaimScenario, Page}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class EndToEndSpec extends Specification with Tags {
  sequential

  def validateAndPrintErrors(page: XmlPage, claim: ClaimScenario) = {
    val errors = page.validateXmlWith(claim)
    println("Number errors: " + errors.size)
    println("List errors: " + errors)
//    println(page.source)
    errors.isEmpty

  }

  "The application " should {
    "Successfully run absolute Test Case 1 " in new WithBrowser with G1BenefitsPageContext {
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 1000, trace = false)
      lastPage match {
        case p: XmlPage => {
          validateAndPrintErrors(p, claim) // must beTrue
        }
        case p: Page => println(p.source)
      }
    }

    "Successfully run absolute Test Case 2 " in new WithBrowser with G1BenefitsPageContext {
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase2.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 1000, trace = false)
      lastPage match {
        case p: XmlPage => {
          validateAndPrintErrors(p, claim) // must beTrue
        }
        case p: Page => println(p.source)
      }
    }

    "Successfully run absolute Test Case 3 " in new WithBrowser with G1BenefitsPageContext {
      try {
        val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase3.csv")
        page goToThePage()
        val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
        lastPage match {
          case p: XmlPage => {
            validateAndPrintErrors(p, claim) // must beTrue
          }
          case p: Page => println(p.source)
        }
      }
      catch {
        case e:PageObjectException => {println(e.message + e.errors)
         println(browser.pageSource())
        }
      }
    }

  } section "functional"

}
