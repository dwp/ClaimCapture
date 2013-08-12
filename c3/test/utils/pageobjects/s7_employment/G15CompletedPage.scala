package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G15CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G15CompletedPage.url, G15CompletedPage.title, previousPage) {

}

object G15CompletedPage {
  val title = "Completion - Employment History".toLowerCase
  val url  = "/employment/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G15CompletedPage(browser,previousPage)
}

trait G15CompletedPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G15CompletedPage buildPageWith browser
}