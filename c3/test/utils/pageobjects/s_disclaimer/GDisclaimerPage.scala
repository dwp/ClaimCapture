package utils.pageobjects.s_disclaimer

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S1 G1 disclaimer.
 */
class GDisclaimerPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GDisclaimerPage.url) {
  // This page does not contain any input elements. This page contains a button and the value is passed to the model when
  // the button is clicked.
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object GDisclaimerPage {
  val url = "/disclaimer/disclaimer"

  def apply(ctx:PageObjectsContext) = new GDisclaimerPage(ctx)
}

/** The context for Specs tests */
trait GDisclaimerPagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GDisclaimerPage (PageObjectsContext(browser))
}
