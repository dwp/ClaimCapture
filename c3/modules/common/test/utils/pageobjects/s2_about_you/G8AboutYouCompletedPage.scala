package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage, TestData, PageContext, Page}

/**
 * PageObject for page s2_about_you g8_completed.
 * @author Jorge Migueis
 *         Date: 18/07/2013
 */
final class G8AboutYouCompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G8AboutYouCompletedPage.url, G8AboutYouCompletedPage.title, previousPage) {
  /**
   * Does nothing. There is no form.
   * @param theClaim   Data to use to fill page
   */
  override  def fillPageWith(theClaim: TestData): Page = this
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G8AboutYouCompletedPage {
  val title = "Completion - About you - the carer".toLowerCase

  val url  = "/about-you/completed"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G8AboutYouCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G8AboutYouCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8AboutYouCompletedPage (browser)
}