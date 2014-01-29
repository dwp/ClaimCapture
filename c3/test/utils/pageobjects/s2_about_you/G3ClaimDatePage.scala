package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * PageObject for page s2_about_you g3_claimDate.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G3ClaimDatePage(ctx:PageObjectsContext) extends ClaimPage(ctx, G3ClaimDatePage.url, G3ClaimDatePage.title) {
  declareDate("#dateOfClaim", "AboutYouWhenDoYouWantYourCarersAllowanceClaimtoStart")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G3ClaimDatePage {
  val title = "Your claim date - About you - the carer".toLowerCase

  val url  = "/about-you/claim-date"

  def apply(ctx:PageObjectsContext) = new G3ClaimDatePage(ctx)
}

/** The context for Specs tests */
trait G3ClaimDatePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3ClaimDatePage (PageObjectsContext(browser))
}