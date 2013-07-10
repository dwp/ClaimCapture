package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject pattern associated to S1 carers allowance G2 Hours page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
class HoursPage(browser: TestBrowser) extends Page(browser, "/allowance/hours", "Hours - Carer's Allowance") {

  def clickChangeBenefitsDetails() = browser.click("div[class=completed] a")

  def isInHoursPage(): Boolean = titleMatch()

  def isQ1Yes(): Boolean = isCompletedYesNo("div[class=completed] ul li", 0, "Q1", "Yes")

  def isQ1No(): Boolean = isCompletedYesNo("div[class=completed] ul li", 0, "Q1", "No")

  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object HoursPage {
  val title = "Hours - Carer's Allowance"
  def buildPage(browser: TestBrowser) = new HoursPage(browser)
}

/** The context for Specs tests */
trait HoursPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = new HoursPage(browser)
}