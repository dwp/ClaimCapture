package utils.pageobjects.s10_pay_details

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G3PayDetailsCompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3PayDetailsCompletedPage.url, G3PayDetailsCompletedPage.title, previousPage) {

}

object G3PayDetailsCompletedPage {

  val title = "Completion - Pay Details"
  val url = "/payDetails/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3PayDetailsCompletedPage(browser, previousPage)

}

trait G3PayDetailsCompletedPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G3PayDetailsCompletedPage buildPageWith browser
}

