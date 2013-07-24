package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject for page s2_about_you g8_completed.
 * @author Jorge Migueis
 *         Date: 18/07/2013
 */
final class G8CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G8CompletedPage.url, G8CompletedPage.title, previousPage) {
  /**
   * Does nothing. There is no form.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G8CompletedPage {
  val title = "Completion - About You"
  val url  = "/aboutyou/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G8CompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G8CompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G8CompletedPage buildPageWith browser
}
