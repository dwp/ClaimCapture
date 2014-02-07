package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

final class G1ReportChangesPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G1ReportChangesPage.url, G1ReportChangesPage.title) {
  declareRadioList("#reportChanges", "CircumstancesReportChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1ReportChangesPage {
  val title = "Details of your change in circumstances - Change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/selection"

  def apply(ctx:PageObjectsContext) = new G1ReportChangesPage(ctx)
}

/** The context for Specs tests */
trait G1ReportChangePagesContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1ReportChangesPage(PageObjectsContext(browser))
}
