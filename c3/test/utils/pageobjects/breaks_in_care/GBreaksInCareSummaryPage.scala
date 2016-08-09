package utils.pageobjects.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

class GBreaksInCareSummaryPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GBreaksInCareSummaryPage.url) {
  declareYesNo("#breaksummary_other", "BreakSummaryOtherYesNo")
  declareRadioList("#breaksummary_answer", "BreakSummaryAnswer")
}

object GBreaksInCareSummaryPage {
  val url  = "/breaks/summary"

  def apply(ctx:PageObjectsContext) = new GBreaksInCareSummaryPage(ctx)
}

trait GBreaksInCareSummaryPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareSummaryPage (PageObjectsContext(browser))
}

