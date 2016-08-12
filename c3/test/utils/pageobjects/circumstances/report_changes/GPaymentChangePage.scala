package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GPaymentChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GPaymentChangePage.url) {
  declareYesNo("#currentlyPaidIntoBankAnswer", "CircumstancesPaymentChangeCurrentlyPaidIntoBank")
  declareInput("#currentlyPaidIntoBankText1", "CircumstancesPaymentChangeNameOfCurrentBank")
  declareInput("#currentlyPaidIntoBankText2", "CircumstancesPaymentCurrentPaymentMethod")
  declareInput("#accountHolderName", "CircumstancesPaymentChangeAccountHolderName")
  declareInput("#bankFullName", "CircumstancesPaymentChangeBankFullName")
  declareSortCode("#sortCode", "CircumstancesPaymentChangeSortCode")
  declareInput("#accountNumber", "CircumstancesPaymentChangeAccountNumber")
  declareInput("#rollOrReferenceNumber", "CircumstancesPaymentChangeRollOrReferenceNumber")
  declareRadioList("#paymentFrequency", "CircumstancesPaymentChangePaymentFrequency")
  declareInput("#moreAboutChanges", "CircumstancesPaymentChangeCaringMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GPaymentChangePage {
  val url  = "/circumstances/report-changes/payment-change"

  def apply(ctx:PageObjectsContext) = new GPaymentChangePage(ctx)
}

/** The context for Specs tests */
trait GPaymentChangePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GPaymentChangePage(PageObjectsContext(browser))
}
