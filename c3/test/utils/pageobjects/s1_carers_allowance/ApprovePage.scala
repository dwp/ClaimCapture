package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject pattern associated to S1 carers allowance G5 approve page.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class ApprovePage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, ApprovePage.url, ApprovePage.title, previousPage) {
  /**
   * Reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}

  def isApproved() =  browser.find("div[class=prompt]").size == 1 && browser.find(".prompt.error]").size == 0
  def isNotApproved() =  browser.find(".prompt.error]").size != 0

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object ApprovePage {
  val title = "Can you get Carer's Allowance?"
  val url = "/allowance/approve"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new ApprovePage(browser, previousPage)
}

/** The context for Specs tests */
trait ApprovePageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = ApprovePage buildPageWith browser
}
