package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class G2SelfEmploymentPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G2SelfEmploymentPage.url) {
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
object G2SelfEmploymentPage {
  val url  = "/circumstances/report-changes/self-employment"

  def apply(ctx:PageObjectsContext) = new G2SelfEmploymentPage(ctx)
}

/** The context for Specs tests */
trait G2SelfEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1ReportChangesPage(PageObjectsContext(browser))
}