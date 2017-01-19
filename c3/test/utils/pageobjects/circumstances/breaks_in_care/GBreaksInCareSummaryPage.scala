package utils.pageobjects.circumstances.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

class GBreaksInCareSummaryPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GBreaksInCareSummaryPage.url) {
  declareCheck("#breaktype_hospital", "BreaktypeHospitalCheckbox")
  declareCheck("#breaktype_carehome", "BreaktypeCareHomeCheckbox")
  declareCheck("#breaktype_none", "BreaktypeNoneCheckbox")
  declareYesNo("#breaktype_other", "BreaktypeOtherYesNo")
}

object GBreaksInCareSummaryPage {
  val url  = "/breaks/breaks-in-care"

  def apply(ctx:PageObjectsContext) = new GBreaksInCareSummaryPage(ctx)
}

trait GBreaksInCareSummaryPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareSummaryPage (PageObjectsContext(browser))
}

