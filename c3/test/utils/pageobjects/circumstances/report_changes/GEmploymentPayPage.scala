package utils.pageobjects.circumstances.report_changes

import controllers.mappings.Mappings
import utils.pageobjects.{TestData, PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GEmploymentPayPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GEmploymentPayPage.url) {
  declareYesNo("#paid", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howmuch", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#paydate", "CircumstancesEmploymentChangeWhatDatePaid")
  declareInput("#whatWasIncluded", "CircumstancesEmploymentChangeWhatWasIncluded")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareInput("#monthlyPayDay", "CircumstancesEmploymentChangeMonthlyPayDay")
  declareYesNo("#sameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
  declareYesNo("#owedMoney", "CircumstancesEmploymentChangeEmployerOwesYouMoney")
  declareInput("#owedMoneyInfo", "CircumstancesEmploymentChangeEmployerOwesYouMoneyInfo")
}

object GEmploymentPayPage {
  val url  = "/circumstances/report-changes/employment-pay"

  def apply(ctx:PageObjectsContext) = new GEmploymentPayPage(ctx)

  def fillPastJobPayDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val jobPage = GEmploymentChangePage.fillPastJobDetails(context, testData => {})
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeBeenPaidYet = Mappings.yes
    claimData.CircumstancesEmploymentChangeHowMuchPaid = "150"
    claimData.CircumstancesEmploymentChangeWhatDatePaid = "01/03/2013"
    claimData.CircumstancesEmploymentChangeHowOftenFrequency = "Weekly"
    claimData.CircumstancesEmploymentChangeUsuallyPaidSameAmount = Mappings.yes
    claimData.CircumstancesEmploymentChangeEmployerOwesYouMoney = Mappings.no
    jobPage.fillPageWith(claimData).submitPage()
  }

  def fillPresentJobPayDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val jobPage = GEmploymentChangePage.fillPresentJobDetails(context, testData => {})
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeBeenPaidYet = Mappings.yes
    claimData.CircumstancesEmploymentChangeHowMuchPaid = "100"
    claimData.CircumstancesEmploymentChangeWhatDatePaid = "01/01/2013"
    claimData.CircumstancesEmploymentChangeHowOftenFrequency = "Weekly"
    claimData.CircumstancesEmploymentChangeUsuallyPaidSameAmount = Mappings.yes
    jobPage.fillPageWith(claimData).submitPage()
  }

  def fillFutureJobPayDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val jobPage = GEmploymentChangePage.fillFutureJobDetails(context, testData => {})
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeBeenPaidYet = Mappings.no
    jobPage.fillPageWith(claimData).submitPage()
  }
}

/** The context for Specs tests */
trait GEmploymentPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentPayPage(PageObjectsContext(browser))
}
