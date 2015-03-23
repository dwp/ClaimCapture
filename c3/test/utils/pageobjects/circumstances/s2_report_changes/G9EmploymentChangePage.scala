package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

final class G9EmploymentChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G9EmploymentChangePage.url) {
  declareYesNo("#stillCaring_answer", "CircumstancesEmploymentChangeStillCaring")
  declareDate("#stillCaring_date", "CircumstancesEmploymentChangeFinishedStillCaringDate")
  declareYesNo("#hasWorkStartedYet_answer", "CircumstancesEmploymentChangeHasWorkStartedYet")
  declareDate("#hasWorkStartedYet_dateWhenStarted", "CircumstancesEmploymentChangeDateWhenStarted")
  declareDate("#hasWorkStartedYet_dateWhenWillItStart", "CircumstancesEmploymentChangeDateWhenWillItStart")
  declareYesNo("#hasWorkFinishedYet_answer", "CircumstancesEmploymentChangeHasWorkFinishedYet")
  declareDate("#hasWorkFinishedYet_dateWhenFinished", "CircumstancesEmploymentChangeDateWhenFinished")
  declareRadioList("#typeOfWork_answer", "CircumstancesEmploymentChangeTypeOfWork")
  declareInput("#typeOfWork_selfEmployedTypeOfWork", "CircumstancesEmploymentChangeSelfEmployedTypeOfWork")
  declareYesNoDontKnow("#typeOfWork_selfEmployedTotalIncome", "CircumstancesEmploymentChangeSelfEmployedTotalIncome")
  declareInput("#typeOfWork_selfEmployedMoreAboutChanges", "CircumstancesEmploymentChangeSelfEmployedMoreAboutChanges")
  declareAddress("#typeOfWork_employerNameAndAddress", "CircumstancesEmploymentChangeEmployerNameAndAddress")
  declareInput("#typeOfWork_employerPostcode", "CircumstancesEmploymentChangeEmployerPostcode")
  declareInput("#typeOfWork_employerContactNumber", "CircumstancesEmploymentChangeEmployerContactNumber")
  declareInput("#typeOfWork_employerPayroll", "CircumstancesEmploymentChangeEmployerPayroll")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G9EmploymentChangePage {
  val url  = "/circumstances/report-changes/employment-change"

  def apply(ctx:PageObjectsContext) = new G9EmploymentChangePage(ctx)
}

/** The context for Specs tests */
trait G9EmploymentChangePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G9EmploymentChangePage(PageObjectsContext(browser))
}