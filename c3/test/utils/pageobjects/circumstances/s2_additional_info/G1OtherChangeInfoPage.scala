package utils.pageobjects.circumstances.s2_additional_info

import play.api.test.WithBrowser
import utils.pageobjects._

final class G1OtherChangeInfoPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G1OtherChangeInfoPage.url, G1OtherChangeInfoPage.title) {
  declareInput("#changeInCircs","CircumstancesOtherChangeInfoChange")
}

object G1OtherChangeInfoPage {
  val title = "Other Change Information - change in circumstances".toLowerCase

  val url  = "/circumstances/additional-info/other-change"

  def apply(ctx:PageObjectsContext) = new G1OtherChangeInfoPage(ctx)
}

trait G1OtherChangeInfoPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1OtherChangeInfoPage(PageObjectsContext(browser))
}

