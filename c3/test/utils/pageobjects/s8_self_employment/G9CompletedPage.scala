package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G9CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G9CompletedPage.url, G9CompletedPage.title, previousPage)

object G9CompletedPage {
  val title = "Completion - Self Employment".toLowerCase

  val url = "/self-employment/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G9CompletedPage(browser, previousPage)
}

trait G9CompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G9CompletedPage buildPageWith browser
}