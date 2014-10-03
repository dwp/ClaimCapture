package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

final class G5PaymentChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G5PaymentChangePage.url, G5PaymentChangePage.title) {
  declareYesNo("#currentlyPaidIntoBank_answer", "CircumstancesPaymentChangeCurrentlyPaidIntoBank")
  declareInput("#currentlyPaidIntoBank_text1", "CircumstancesPaymentChangeNameOfCurrentBank")
  declareInput("#currentlyPaidIntoBank_text2", "CircumstancesPaymentCurrentPaymentMethod")
  declareInput("#accountHolderName", "CircumstancesPaymentChangeAccountHolderName")
  declareInput("#bankFullName", "CircumstancesPaymentChangeBankFullName")
  declareSortCode("#sortCode", "CircumstancesPaymentChangeSortCode")
  declareInput("#accountNumber", "CircumstancesPaymentChangeAccountNumber")
  declareInput("#rollOrReferenceNumber", "CircumstancesPaymentChangeRollOrReferenceNumber")
  declareSelect("#paymentFrequency", "CircumstancesPaymentChangePaymentFrequency")
  declareInput("#moreAboutChanges", "CircumstancesPaymentChangeCaringMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5PaymentChangePage {
  val title = "Existing payment details - Change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/payment-change"

  def apply(ctx:PageObjectsContext) = new G5PaymentChangePage(ctx)
}

/** The context for Specs tests */
trait G5PaymentChangePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5PaymentChangePage(PageObjectsContext(browser))
}