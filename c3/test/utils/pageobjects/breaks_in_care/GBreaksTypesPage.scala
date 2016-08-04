package utils.pageobjects.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

class GBreaksTypesPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GBreaksTypesPage.url) {
  declareCheck("#breaktype_hospital", "BreaktypeHospitalCheckbox")
  declareCheck("#breaktype_carehome", "BreaktypeCarehomeCheckbox")
  declareCheck("#breaktype_none", "BreaktypeNoneCheckbox")
  declareYesNo("#breaktype_other", "BreaktypeOtherYesNo")
}

object GBreaksTypesPage {
  val url  = "/breaks/new-breaks-in-care"

  def apply(ctx:PageObjectsContext) = new GBreaksTypesPage(ctx)
}

trait GBreaksTypesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksTypesPage (PageObjectsContext(browser))
}

