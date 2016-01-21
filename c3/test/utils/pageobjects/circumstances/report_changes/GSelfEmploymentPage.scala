package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.circumstances.start_of_process.GReportChangesPage
import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GSelfEmploymentPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GSelfEmploymentPage.url) {
  declareYesNo("#stillCaring_answer", "CircumstancesSelfEmploymentStillCaring")
  declareDate("#stillCaring_date", "CircumstancesSelfEmploymentFinishedStillCaringDate")
  declareDate("#whenThisSelfEmploymentStarted", "CircumstancesSelfEmploymentWhenThisStarted")
  declareInput("#typeOfBusiness", "CircumstancesSelfEmploymentTypeOfBusiness")
  declareYesNoDontKnow("#totalOverWeeklyIncomeThreshold", "CircumstancesSelfEmploymentTotalOverWeeklyIncomeThreshold")
  declareInput("#moreAboutChanges", "CircumstancesSelfEmploymentMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GSelfEmploymentPage {
  val url  = "/circumstances/report-changes/self-employment"

  def apply(ctx:PageObjectsContext) = new GSelfEmploymentPage(ctx)
}

/** The context for Specs tests */
trait GSelfEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GReportChangesPage(PageObjectsContext(browser))
}
