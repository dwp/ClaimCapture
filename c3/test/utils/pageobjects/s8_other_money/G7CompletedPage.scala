package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G7CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G7CompletedPage.url, G7CompletedPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {

  }
}

object G7CompletedPage {

  val title = "Completion - Other Money"
  val url = "/otherMoney/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7CompletedPage(browser, previousPage)

}

trait G7CompletedPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G7CompletedPage buildPageWith browser
}

