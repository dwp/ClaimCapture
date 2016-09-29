package utils.pageobjects.s_claim_date

import controllers.ClaimScenarioFactory._
import utils.WithBrowser
import utils.pageobjects._



/**
 * PageObject for page s_claim_date g_claimDate.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class GClaimDatePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GClaimDatePage.url) {
  declareDate("#dateOfClaim", "ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart")
  declareYesNo("#beforeClaimCaring_answer", "ClaimDateDidYouCareForThisPersonfor35Hours")
  declareDate("#beforeClaimCaring_date", "ClaimDateWhenDidYouStartToCareForThisPerson")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object GClaimDatePage {
  val url  = "/your-claim-date/claim-date"

  def apply(ctx:PageObjectsContext) = new GClaimDatePage(ctx)

  def fillClaimDate(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = s12ClaimDate()
    f(claimData)
    val page = new GClaimDatePage(context) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }
}

/** The context for Specs tests */
trait GClaimDatePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GClaimDatePage (PageObjectsContext(browser))
}
