package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject pattern associated to S1 carers allowance G5 approve page.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class ApprovePage(browser: TestBrowser, title: String) extends Page(browser, "/allowance/approve", title) {
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
object ApprovePage {
  val title = "Can you get Carer's Allowance?"
  def buildPage(browser: TestBrowser) = new ApprovePage(browser, title)
}

/** The context for Specs tests */
trait ApprovePageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = ApprovePage buildPage (browser)
}
