package utils.pageobjects.s_pay_details

import utils.WithBrowser
import utils.pageobjects._

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 18/07/2013
 */
class GHowWePayYouPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GHowWePayYouPage.url) {
  declareRadioList("#likeToPay", "HowWePayYouHowWouldYouLikeToGetPaid")
  declareRadioList("#paymentFrequency", "HowWePayYouHowOftenDoYouWantToGetPaid")

  declareInput("#bankDetails_accountHolderName", "HowWePayYouNameOfAccountHolder")
  declareInput("#bankDetails_bankFullName", "HowWePayYouFullNameOfBankorBuildingSociety")
  declareSortCode("#bankDetails_sortCode", "HowWePayYouSortCode")
  declareInput("#bankDetails_accountNumber", "HowWePayYouAccountNumber")
  declareInput("#bankDetails_rollOrReferenceNumber", "HowWePayYouBuildingSocietyRollOrReferenceNumber")

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object d
 * efined in Page.scala
 */
object GHowWePayYouPage {
  val url  = "/pay-details/how-we-pay-you"

  def apply(ctx:PageObjectsContext) = new GHowWePayYouPage(ctx)
}

/** The context for Specs tests */
trait GHowWePayYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GHowWePayYouPage (PageObjectsContext(browser))
}
