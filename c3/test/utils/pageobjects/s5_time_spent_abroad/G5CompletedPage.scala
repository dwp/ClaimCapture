package utils.pageobjects.s5_time_spent_abroad

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g5_completed.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G5CompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5CompletedPage.url, G5CompletedPage.title, previousPage) {
  /**
   * Does nothing. There is no form.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5CompletedPage {
  val title = "Completion - Time Spent Abroad"
  val url  = "/timeSpentAbroad/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5CompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5CompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G5CompletedPage buildPageWith browser
}
