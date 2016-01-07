package utils.pageobjects.circumstances.start_of_process

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GReportChangesPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GReportChangesPage.url) {
  declareRadioList("#reportChanges", "CircumstancesReportChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GReportChangesPage {
  val url  = "/circumstances/report-changes/selection"

  def apply(ctx:PageObjectsContext) = new GReportChangesPage(ctx)
}

/** The context for Specs tests */
trait GReportChangePagesContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GReportChangesPage(PageObjectsContext(browser))
}
