package utils.pageobjects.s_pay_details

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page object for s_pay_details, GBankBuildingSocietyDetailsPage.
 * @author Saqib Kayani
 *         Date: 01/08/2013
 */
final class GBankBuildingSocietyDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GBankBuildingSocietyDetailsPage.url) {
  declareInput("#accountHolderName", "HowWePayYouNameOfAccountHolder")
  declareInput("#bankFullName", "HowWePayYouFullNameOfBankorBuildingSociety")
  declareSortCode("#sortCode", "HowWePayYouSortCode")
  declareInput("#accountNumber", "HowWePayYouAccountNumber")
  declareInput("#rollOrReferenceNumber", "HowWePayYouBuildingSocietyRollOrReferenceNumber")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GBankBuildingSocietyDetailsPage {
  val url  = "/pay-details/bank-building-society-details"

  def apply(ctx:PageObjectsContext) = new GBankBuildingSocietyDetailsPage(ctx)
}

/** The context for Specs tests */
trait GBankBuildingSocietyDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBankBuildingSocietyDetailsPage (PageObjectsContext(browser))
}