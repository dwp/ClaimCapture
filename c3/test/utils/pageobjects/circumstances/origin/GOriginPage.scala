package utils.pageobjects.circumstances.origin

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GOriginPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GOriginPage.url) {
  declareRadioList("#origin", "CircsOrigin")
}

object GOriginPage {
  val url  = "/circumstances/report-changes/selection"

  def apply(ctx:PageObjectsContext) = new GOriginPage(ctx)
}

trait GOriginPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GOriginPage(PageObjectsContext(browser))
}

