package utils.pageobjects.S11_consent_and_declaration

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G5 Submit page.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G5SubmitPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G5SubmitPage.url, G5SubmitPage.title)

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G5SubmitPage {
  val title = "Documents you need to send us - Consent and Declaration".toLowerCase

  val url = "/consent-and-declaration/submit"

  def apply(ctx:PageObjectsContext) = new G5SubmitPage(ctx)
}

/** The context for Specs tests */
trait G5SubmitPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5SubmitPage (PageObjectsContext(browser))
}