package utils.pageobjects.s11_pay_details

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page object for s11_pay_details, G2BankBuildingSocietyDetailsPage.
 * @author Saqib Kayani
 *         Date: 01/08/2013
 */
final class G2BankBuildingSocietyDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G2BankBuildingSocietyDetailsPage.url) {
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
object G2BankBuildingSocietyDetailsPage {
  val url  = "/pay-details/bank-building-society-details"

  def apply(ctx:PageObjectsContext) = new G2BankBuildingSocietyDetailsPage(ctx)
}

/** The context for Specs tests */
trait G2BankBuildingSocietyDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2BankBuildingSocietyDetailsPage (PageObjectsContext(browser))
}