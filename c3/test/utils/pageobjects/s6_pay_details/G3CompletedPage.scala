package utils.pageobjects.s6_pay_details

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G3CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3CompletedPage.url, G3CompletedPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {
  }

}

object G3CompletedPage {

  val title = "Completion - Pay Details"
  val url = "/payDetails/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3CompletedPage(browser, previousPage)

}

trait G3CompletedPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G3CompletedPage buildPageWith browser
}

