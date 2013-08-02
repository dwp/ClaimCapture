package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G8OtherMoneyCompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G8OtherMoneyCompletedPage.url, G8OtherMoneyCompletedPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {

  }
}

object G8OtherMoneyCompletedPage {

  val title = "Completion - Other Money"
  val url = "/otherMoney/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G8OtherMoneyCompletedPage(browser, previousPage)

}

trait G8OtherMoneyCompletedPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G8OtherMoneyCompletedPage buildPageWith browser
}

