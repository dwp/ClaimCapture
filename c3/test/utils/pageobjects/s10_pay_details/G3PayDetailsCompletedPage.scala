package utils.pageobjects.s10_pay_details

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G3PayDetailsCompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3PayDetailsCompletedPage.url, G3PayDetailsCompletedPage.title, previousPage)

object G3PayDetailsCompletedPage {
  val title = "Completion - How we pay you".toLowerCase

  val url = "/pay-details/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3PayDetailsCompletedPage(browser, previousPage)
}

trait G3PayDetailsCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3PayDetailsCompletedPage buildPageWith browser
}