package utils.pageobjects.s5_time_spent_abroad

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g5_completed.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G5TimeAbroadCompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5TimeAbroadCompletedPage.url, G5TimeAbroadCompletedPage.title, previousPage) {

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5TimeAbroadCompletedPage {
  val title = "Completion - Time Spent Abroad"
  val url  = "/timeSpentAbroad/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5TimeAbroadCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5TimeAbroadCompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G5TimeAbroadCompletedPage buildPageWith browser
}
