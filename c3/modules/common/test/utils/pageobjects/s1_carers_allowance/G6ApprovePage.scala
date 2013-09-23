package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * PageObject pattern associated to S1 carers allowance G5 approve page.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
final class G6ApprovePage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G6ApprovePage.url, G6ApprovePage.title, previousPage) {
  def isApproved =  browser.find(".prompt").size != 0 && browser.find(".prompt.error]").size == 0

  def isNotApproved =  browser.find(".prompt.error]").size != 0
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G6ApprovePage {
  val title = "Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/approve"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G6ApprovePage(browser, previousPage)
}

/** The context for Specs tests */
trait G6ApprovePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6ApprovePage (browser)
}