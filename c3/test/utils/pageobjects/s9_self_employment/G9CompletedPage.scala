package utils.pageobjects.s9_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G9CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G9CompletedPage.url, G9CompletedPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {
    // None
  }
}

object G9CompletedPage {
  val title = "Self Employment - Completion"
  val url = "/selfEmployment/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G9CompletedPage(browser, previousPage)
}

trait G9CompletedPagePageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G9CompletedPage buildPageWith browser
}
