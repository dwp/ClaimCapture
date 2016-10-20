package utils.pageobjects.circumstances.report_changes

import controllers.mappings.Mappings
import utils.pageobjects.{TestData, PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GStartedEmploymentAndOngoingPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GStartedEmploymentAndOngoingPage.url) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#whatDatePaid", "CircumstancesEmploymentChangeWhatDatePaid")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareInput("#monthlyPayDay", "CircumstancesEmploymentChangeMonthlyPayDay")
  declareYesNo("#usuallyPaidSameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GStartedEmploymentAndOngoingPage {
  val url  = "/circumstances/report-changes/employment-ongoing"

  def apply(ctx:PageObjectsContext) = new GStartedEmploymentAndOngoingPage(ctx)

  def fillJobPayDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val presentJobPage = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeBeenPaidYet = Mappings.yes
    claimData.CircumstancesEmploymentChangeHowMuchPaid = "100"
    claimData.CircumstancesEmploymentChangeWhatDatePaid = "01/01/2013"
    claimData.CircumstancesEmploymentChangeHowOftenFrequency = "Weekly"
    claimData.CircumstancesEmploymentChangeUsuallyPaidSameAmount = Mappings.yes
    presentJobPage.fillPageWith(claimData).submitPage()
  }
}

/** The context for Specs tests */
trait GStartedEmploymentAndOngoingPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GStartedEmploymentAndOngoingPage(PageObjectsContext(browser))
}
