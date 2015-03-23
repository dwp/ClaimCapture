package utils.pageobjects.s1_disclaimer

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G3 disclaimer.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G1DisclaimerPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G1DisclaimerPage.url, G1DisclaimerPage.title) {
  declareInput("#read", "DisclaimerUnderstandAndAgree")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1DisclaimerPage {
  val title = "Disclaimer - Before you start".toLowerCase

  val url = "/disclaimer/disclaimer"

  def apply(ctx:PageObjectsContext) = new G1DisclaimerPage(ctx)
}

/** The context for Specs tests */
trait G1DisclaimerPagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1DisclaimerPage (PageObjectsContext(browser))
}