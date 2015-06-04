package utils.pageobjects.s1_disclaimer

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S1 G1 disclaimer.
 */
class G1DisclaimerPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G1DisclaimerPage.url) {
  // This page does not contain any input elements. This page contains a button and the value is passed to the model when
  // the button is clicked.
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1DisclaimerPage {
  val url = "/disclaimer/disclaimer"

  def apply(ctx:PageObjectsContext) = new G1DisclaimerPage(ctx)
}

/** The context for Specs tests */
trait G1DisclaimerPagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1DisclaimerPage (PageObjectsContext(browser))
}