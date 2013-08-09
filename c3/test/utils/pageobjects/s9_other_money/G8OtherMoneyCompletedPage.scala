package utils.pageobjects.s9_other_money

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G8OtherMoneyCompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G8OtherMoneyCompletedPage.url, G8OtherMoneyCompletedPage.title, previousPage)

object G8OtherMoneyCompletedPage {
  val title = "Completion - Other Money"
  val url = "/otherMoney/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G8OtherMoneyCompletedPage(browser, previousPage)
}

trait G8OtherMoneyCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8OtherMoneyCompletedPage buildPageWith browser
}