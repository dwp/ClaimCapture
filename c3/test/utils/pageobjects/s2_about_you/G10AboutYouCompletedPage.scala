package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage, TestData, PageContext, Page}

/**
 * PageObject for page s2_about_you g10_completed.
 * @author Jorge Migueis
 *         Date: 18/07/2013
 */
final class G10AboutYouCompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G10AboutYouCompletedPage.url, G10AboutYouCompletedPage.title, previousPage) {
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
object G10AboutYouCompletedPage {
  val title = "Completion - About you - the carer".toLowerCase

  val url  = "/about-you/completed"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G10AboutYouCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G10AboutYouCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G10AboutYouCompletedPage (browser)
}