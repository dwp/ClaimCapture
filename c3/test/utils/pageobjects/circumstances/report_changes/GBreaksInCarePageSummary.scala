package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

/**
 * Created by mhunter on 26/03/14.
 */
final class GBreaksInCareSummaryPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GBreaksInCareSummaryPage.url) {
  declareYesNo("#additionalBreaks_answer", "BreaksInCareSummaryAdditionalBreaks")
  declareInput("#additionalBreaks_text", "BreaksInCareSummaryAdditionalBreaksInfo")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GBreaksInCareSummaryPage {
  val url  = "/circumstances/report-changes/breaks-in-care-summary"

  def apply(ctx:PageObjectsContext) = new GBreaksInCareSummaryPage(ctx)
}

/** The context for Specs tests */
trait GBreaksInCareSummaryPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareSummaryPage(PageObjectsContext(browser))
}


