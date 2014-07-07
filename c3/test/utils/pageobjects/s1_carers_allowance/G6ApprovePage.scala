package utils.pageobjects.s1_carers_allowance

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * PageObject pattern associated to S1 carers allowance G5 approve page.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
final class G6ApprovePage(ctx:PageObjectsContext) extends ClaimPage(ctx, G6ApprovePage.url, G6ApprovePage.title) {
  def isApproved =  ctx.browser.find(".prompt.helper-prompt").size != 0 && ctx.browser.find(".prompt.entitlement-error").size == 0

  def isNotApproved =  ctx.browser.find(".prompt.entitlement-error").size != 0
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G6ApprovePage {
  val title = "Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/approve"

  def apply(ctx:PageObjectsContext) = new G6ApprovePage(ctx)
}

/** The context for Specs tests */
trait G6ApprovePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6ApprovePage (PageObjectsContext(browser))
}