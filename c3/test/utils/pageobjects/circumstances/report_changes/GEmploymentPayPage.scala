package utils.pageobjects.circumstances.report_changes

import controllers.mappings.Mappings
import utils.pageobjects.{TestData, PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GEmploymentPayPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GEmploymentPayPage.url) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#dateLastPaid", "CircumstancesEmploymentChangeWhatDatePaid")
  declareInput("#whatWasIncluded", "CircumstancesEmploymentChangeWhatWasIncluded")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareInput("#monthlyPayDay", "CircumstancesEmploymentChangeMonthlyPayDay")
  declareYesNo("#usuallyPaidSameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
  declareYesNo("#employerOwesYouMoney", "CircumstancesEmploymentChangeEmployerOwesYouMoney")
  declareInput("#employerOwesYouMoneyInfo", "CircumstancesEmploymentChangeEmployerOwesYouMoneyInfo")
}

object GEmploymentPayPage {
  val url  = "/circumstances/report-changes/employment-pay"

  def apply(ctx:PageObjectsContext) = new GEmploymentPayPage(ctx)

  def fillPastJobPayDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val pastJobPage = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeBeenPaidYet = Mappings.yes
    claimData.CircumstancesEmploymentChangeHowMuchPaid = "150"
    claimData.CircumstancesEmploymentChangeWhatDatePaid = "01/03/2013"
    claimData.CircumstancesEmploymentChangeHowOftenFrequency = "Weekly"
    claimData.CircumstancesEmploymentChangeUsuallyPaidSameAmount = Mappings.yes
    claimData.CircumstancesEmploymentChangeEmployerOwesYouMoney = Mappings.no
    pastJobPage.fillPageWith(claimData).submitPage()
  }
}

/** The context for Specs tests */
trait GEmploymentPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentPayPage(PageObjectsContext(browser))
}
