package utils.pageobjects.s5_time_spent_abroad

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g5_completed.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G5TimeAbroadCompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5TimeAbroadCompletedPage.url, G5TimeAbroadCompletedPage.title, previousPage)

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5TimeAbroadCompletedPage {
  val title = "Completion - Time Spent Abroad".toLowerCase

  val url  = "/time-spent-abroad/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5TimeAbroadCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5TimeAbroadCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5TimeAbroadCompletedPage buildPageWith browser
}