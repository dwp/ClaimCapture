package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GEmploymentChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GEmploymentChangePage.url) {
  declareYesNo("#stillCaring_answer", "CircumstancesEmploymentChangeStillCaring")
  declareDate("#stillCaring_date", "CircumstancesEmploymentChangeFinishedStillCaringDate")
  declareYesNo("#hasWorkStartedYet_answer", "CircumstancesEmploymentChangeHasWorkStartedYet")
  declareDate("#hasWorkStartedYet_dateWhenStarted", "CircumstancesEmploymentChangeDateWhenStarted")
  declareDate("#hasWorkStartedYet_dateWhenWillItStart", "CircumstancesEmploymentChangeDateWhenWillItStart")
  declareYesNo("#hasWorkFinishedYet_answer", "CircumstancesEmploymentChangeHasWorkFinishedYet")
  declareDate("#hasWorkFinishedYet_dateWhenFinished", "CircumstancesEmploymentChangeDateWhenFinished")
  declareRadioList("#typeOfWork_answer", "CircumstancesEmploymentChangeTypeOfWork")
  declareInput("#typeOfWork_selfEmployedTypeOfWork", "CircumstancesEmploymentChangeSelfEmployedTypeOfWork")
  declareYesNo("#paidMoneyYet_answer", "CircumstancesEmploymentChangeSelfEmployedPaidMoneyYet")
  declareDate("#paidMoneyYet_date", "CircumstancesEmploymentChangeSelfEmployedPaidMoneyDate")
  declareYesNoDontKnow("#typeOfWork_selfEmployedTotalIncome", "CircumstancesEmploymentChangeSelfEmployedTotalIncome")
  declareInput("#typeOfWork_selfEmployedMoreAboutChanges", "CircumstancesEmploymentChangeSelfEmployedMoreAboutChanges")
  declareInput("#typeOfWork_employerName", "CircumstancesEmploymentChangeEmployerName")
  declareAddress("#typeOfWork_employerNameAndAddress", "CircumstancesEmploymentChangeEmployerNameAndAddress")
  declareInput("#typeOfWork_employerPostcode", "CircumstancesEmploymentChangeEmployerPostcode")
  declareInput("#typeOfWork_employerContactNumber", "CircumstancesEmploymentChangeEmployerContactNumber")
  declareInput("#typeOfWork_employerPayroll", "CircumstancesEmploymentChangeEmployerPayroll")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GEmploymentChangePage {
  val url  = "/circumstances/report-changes/employment-change"

  def apply(ctx:PageObjectsContext) = new GEmploymentChangePage(ctx)
}

/** The context for Specs tests */
trait GEmploymentChangePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentChangePage(PageObjectsContext(browser))
}
