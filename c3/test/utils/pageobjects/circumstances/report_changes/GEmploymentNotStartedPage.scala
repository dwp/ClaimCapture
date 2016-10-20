package utils.pageobjects.circumstances.report_changes

import controllers.mappings.Mappings
import utils.pageobjects.{TestData, CircumstancesPage, PageContext, PageObjectsContext}
import utils.WithBrowser

class GEmploymentNotStartedPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GEmploymentNotStartedPage.url) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#whenExpectedToBePaidDate", "CircumstancesEmploymentChangeWhatDatePaid")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareYesNo("#usuallyPaidSameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GEmploymentNotStartedPage {
  val url  = "/circumstances/report-changes/future-employment"

  def apply(ctx:PageObjectsContext) = new GEmploymentNotStartedPage(ctx)

  def fillJobPayDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val futureJobPage = GEmploymentChangePage.fillFutureJobDetails(context, testData => {})
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeBeenPaidYet = Mappings.no
    futureJobPage.fillPageWith(claimData).submitPage()
  }
}

/** The context for Specs tests */
trait GEmploymentNotStartedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentNotStartedPage(PageObjectsContext(browser))
}
