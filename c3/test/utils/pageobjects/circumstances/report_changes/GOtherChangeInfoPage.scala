package utils.pageobjects.circumstances.report_changes

import utils.WithBrowser
import utils.pageobjects._

final class GOtherChangeInfoPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GOtherChangeInfoPage.url) {
  declareInput("#changeInCircs","CircumstancesOtherChangeInfoChange")
}

object GOtherChangeInfoPage {
  val url  = "/circumstances/report-changes/other-change"

  def apply(ctx:PageObjectsContext) = new GOtherChangeInfoPage(ctx)
}

trait GOtherChangeInfoPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GOtherChangeInfoPage(PageObjectsContext(browser))
}

