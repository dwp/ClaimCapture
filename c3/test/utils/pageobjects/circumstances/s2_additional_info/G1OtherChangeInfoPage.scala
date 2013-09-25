package utils.pageobjects.circumstances.s2_additional_info

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{CircumstancesPage, PageContext, Page}

final class G1OtherChangeInfoPage(browser: TestBrowser, previousPage: Option[Page] = None) extends CircumstancesPage(browser, G1OtherChangeInfoPage.url, G1OtherChangeInfoPage.title, previousPage) {
  declareInput("#changeInCircs","CircumstancesOtherChangeInfoChange")
}

object G1OtherChangeInfoPage {
  val title = "Other Change Information - Additional Info".toLowerCase

  val url  = "/circumstances/additional-info/other-change"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G1OtherChangeInfoPage(browser, previousPage)
}

trait G1OtherChangeInfoPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1OtherChangeInfoPage(browser)
}

