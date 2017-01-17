package utils.pageobjects.circumstances.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

class GCircsBreaksInCareSummaryPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GCircsBreaksInCareSummaryPage.url) {
  declareCheck("#breaktype_hospital", "BreaktypeHospitalCheckbox")
  declareCheck("#breaktype_carehome", "BreaktypeCareHomeCheckbox")
  declareCheck("#breaktype_none", "BreaktypeNoneCheckbox")
  declareYesNo("#breaktype_other", "BreaktypeOtherYesNo")
}

object GCircsBreaksInCareSummaryPage {
  val url  = "/circumstances/breaks/breaks-in-care"

  def apply(ctx:PageObjectsContext) = new GCircsBreaksInCareSummaryPage(ctx)
}

trait GCircsBreaksInCareSummaryPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GCircsBreaksInCareSummaryPage (PageObjectsContext(browser))
}

