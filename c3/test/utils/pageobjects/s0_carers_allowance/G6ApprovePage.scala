package utils.pageobjects.s0_carers_allowance

import utils.WithBrowser
import utils.pageobjects._
import org.fluentlenium.core.domain.{FluentWebElement, FluentList}
import play.api.Logger

/**
 * PageObject pattern associated to S0 carers allowance G5 approve page.
 */
final class G6ApprovePage(ctx:PageObjectsContext) extends ClaimPage(ctx, G6ApprovePage.url) {
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
object G6ApprovePage {
  val url = "/allowance/approve"

  def apply(ctx:PageObjectsContext) = new G6ApprovePage(ctx)
}

/** The context for Specs tests */
trait G6ApprovePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6ApprovePage (PageObjectsContext(browser))
}