package utils.pageobjects.s12_consent_and_declaration

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G3 disclaimer.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G2DisclaimerPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G2DisclaimerPage.url, G2DisclaimerPage.title) {
  declareCheck("#read", "ConsentDeclarationDisclaimerTextAndTickBox")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G2DisclaimerPage {
  val title = "Disclaimer - Consent and Declaration".toLowerCase

  val url = "/consent-and-declaration/disclaimer"

  def apply(ctx:PageObjectsContext) = new G2DisclaimerPage(ctx)
}

/** The context for Specs tests */
trait G2DisclaimerPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2DisclaimerPage (PageObjectsContext(browser))
}