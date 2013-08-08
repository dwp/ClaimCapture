package utils.pageobjects.s8_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G9CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G9CompletedPage.url, G9CompletedPage.title, previousPage) {

}

object G9CompletedPage {
  val title = "Completion - Self Employment"
  val url = "/selfEmployment/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G9CompletedPage(browser, previousPage)
}

trait G9CompletedPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G9CompletedPage buildPageWith browser
}
