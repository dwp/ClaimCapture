package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G2BeenEmployedPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends ClaimPage(browser, G2BeenEmployedPage.url, G2BeenEmployedPage.title, previousPage, iteration) {
  declareYesNo("#beenEmployed", "AboutYouHaveYouBeenEmployedAtAnyTime_"+iteration)
}

object G2BeenEmployedPage {
  val title = "Your employment history - Employment History".toLowerCase

  val url  = "/employment/been-employed"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G2BeenEmployedPage(browser,previousPage,iteration)
}

trait G2BeenEmployedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2BeenEmployedPage (browser,iteration=1)
}