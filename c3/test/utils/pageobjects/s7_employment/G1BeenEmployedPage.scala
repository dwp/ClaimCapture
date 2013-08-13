package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G1BeenEmployedPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G1BeenEmployedPage.url, G1BeenEmployedPage.title, previousPage, iteration) {
  declareYesNo("#beenEmployed", "AboutYouHaveYouBeenEmployedAtAnyTime_"+iteration)
}

object G1BeenEmployedPage {
  val title = "Your employment history - Employment History".toLowerCase

  val url  = "/employment/been-employed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G1BeenEmployedPage(browser,previousPage,iteration)
}

trait G1BeenEmployedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1BeenEmployedPage buildPageWith (browser,iteration=1)
}