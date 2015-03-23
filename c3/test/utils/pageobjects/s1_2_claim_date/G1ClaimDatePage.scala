package utils.pageobjects.s1_2_claim_date

import play.api.test.WithBrowser
import utils.pageobjects._



/**
 * PageObject for page s2_about_you g1_claimDate.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G1ClaimDatePage(ctx:PageObjectsContext) extends ClaimPage(ctx, G1ClaimDatePage.url) {
  declareDate("#dateOfClaim", "ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1ClaimDatePage {
  val url  = "/your-claim-date/claim-date"

  def apply(ctx:PageObjectsContext) = new G1ClaimDatePage(ctx)
}

/** The context for Specs tests */
trait G1ClaimDatePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1ClaimDatePage (PageObjectsContext(browser))
}