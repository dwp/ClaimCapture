package utils.pageobjects.s_eligibility

import utils.WithBrowser
import utils.pageobjects._
import org.fluentlenium.core.domain.{FluentWebElement, FluentList}
import play.api.Logger

/**
 * PageObject pattern associated to S0 carers allowance G5 approve page.
 */
final class GApprovePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GApprovePage.url) {
  declareYesNo("#answer", "CanYouGetCarersAllowanceApproveAnswer")

  def isApproved = {
    ctx.browser.find("#allowedToContinue").getValue == "true"
  }

  def isNotApproved = !isApproved
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object GApprovePage {
  val url = "/allowance/approve"

  def apply(ctx:PageObjectsContext) = new GApprovePage(ctx)
}

/** The context for Specs tests */
trait GApprovePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GApprovePage (PageObjectsContext(browser))
}
