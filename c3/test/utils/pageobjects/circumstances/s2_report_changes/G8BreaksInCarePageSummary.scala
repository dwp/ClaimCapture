package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

/**
 * Created by mhunter on 26/03/14.
 */
final class G8BreaksInCareSummaryPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G8BreaksInCareSummaryPage.url) {
  declareYesNo("#additionalBreaks_answer", "BreaksInCareSummaryAdditionalBreaks")
  declareInput("#additionalBreaks_text", "BreaksInCareSummaryAdditionalBreaksInfo")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G8BreaksInCareSummaryPage {
  val url  = "/circumstances/report-changes/breaks-in-care-summary"

  def apply(ctx:PageObjectsContext) = new G8BreaksInCareSummaryPage(ctx)
}

/** The context for Specs tests */
trait G8BreaksInCareSummaryPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8BreaksInCareSummaryPage(PageObjectsContext(browser))
}


