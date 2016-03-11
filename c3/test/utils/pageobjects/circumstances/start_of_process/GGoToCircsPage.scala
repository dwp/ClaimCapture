package utils.pageobjects.circumstances.start_of_process

import utils.pageobjects.{CircumstancesPage, PageObjectsContext}

final class GGoToCircsPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GGoToCircsPage.url) {
}

object GGoToCircsPage {
  val url  = "/circumstances/report-changes/gotofunction"
}

