package utils.pageobjects.s3_your_partner

import utils.WithBrowser
import utils.pageobjects._

/**
 * PageObject for page s_about_you g10_completed.
 * @author Saqib Kayani
 *         Date: 24/07/2013
 */
final class G5YourPartnerCompletedPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G5YourPartnerCompletedPage.url)

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G5YourPartnerCompletedPage {
  val url  = "/your-partner/completed"

  def apply(ctx:PageObjectsContext) = new G5YourPartnerCompletedPage(ctx)
}

/** The context for Specs tests */
trait G5YourPartnerCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5YourPartnerCompletedPage (PageObjectsContext(browser))
}