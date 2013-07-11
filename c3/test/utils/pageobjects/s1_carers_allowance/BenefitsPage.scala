package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ComponentObject, ClaimScenario, PageContext, Page}

/**
 * PageObject pattern associated to S1 carers allowance G1 Benefits page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
class BenefitsPage(browser: TestBrowser) extends Page(browser, "/", BenefitsPage.title) with ComponentObject {

  def clickPersonGetsBenefits() = browser.click("#q3-yes")

  def clickPersonDoesNotGetBenefits() = browser.click("#q3-no")

  def doesPersonGetBenefit() = valueOfYesNo("#q3-yes")

  def isInBenefitsPage(): Boolean = titleMatch()

  def hasQ1() = browser.find("div[class=carers-allowance]").getText contains "Q1"

  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    theClaim.`Can you get Carers Allowance? - Does the person you care for get one of these benefits?` match {
      case "Yes" => browser.click("#q3-yes")
      case "No" => browser.click("#q3-no")
    }
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object BenefitsPage {
  val title = "Benefits - Carer's Allowance"
  def buildPage(browser: TestBrowser) = new BenefitsPage(browser)
}

/** The context for Specs tests */
trait BenefitsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = BenefitsPage buildPage (browser)
}