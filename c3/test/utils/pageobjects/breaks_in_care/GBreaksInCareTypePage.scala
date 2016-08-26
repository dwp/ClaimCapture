package utils.pageobjects.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

class GBreaksInCareTypePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GBreaksInCareTypePage.url) {
  declareCheck("#breaktype_hospital", "BreaktypeHospitalCheckbox")
  declareCheck("#breaktype_carehome", "BreaktypeCarehomeCheckbox")
  declareCheck("#breaktype_none", "BreaktypeNoneCheckbox")
  declareYesNo("#breaktype_other", "BreaktypeOtherYesNo")
}

object GBreaksInCareTypePage {
  val url  = "/breaks/new-breaks-in-care"

  def apply(ctx:PageObjectsContext) = new GBreaksInCareTypePage(ctx)
}

trait GBreaksTypesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareTypePage (PageObjectsContext(browser))
}

