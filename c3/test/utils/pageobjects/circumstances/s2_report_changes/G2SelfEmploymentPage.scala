package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

final class G2SelfEmploymentPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G2SelfEmploymentPage.url, G2SelfEmploymentPage.title) {
  declareSelect("#reportChanges", "CircumstancesReportChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2SelfEmploymentPage {
  val title = "Details of your change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/selection"

  def apply(ctx:PageObjectsContext) = new G3PermanentlyStoppedCaringPage(ctx)
}

/** The context for Specs tests */
trait G2SelfEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1ReportChangesPage(PageObjectsContext(browser))
}