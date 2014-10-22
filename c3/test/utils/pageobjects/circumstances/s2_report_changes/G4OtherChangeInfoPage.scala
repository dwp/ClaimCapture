package utils.pageobjects.circumstances.s2_report_changes

import play.api.test.WithBrowser
import utils.pageobjects._

final class G4OtherChangeInfoPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G4OtherChangeInfoPage.url, G4OtherChangeInfoPage.title) {
  declareInput("#changeInCircs","CircumstancesOtherChangeInfoChange")
}

object G4OtherChangeInfoPage {
  val title = "Other Changes - change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/other-change"

  def apply(ctx:PageObjectsContext) = new G4OtherChangeInfoPage(ctx)
}

trait G4OtherChangeInfoPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4OtherChangeInfoPage(PageObjectsContext(browser))
}

