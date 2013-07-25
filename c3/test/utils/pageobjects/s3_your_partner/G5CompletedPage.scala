package utils.pageobjects.s3_your_partner

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject for page s2_about_you g8_completed.
 * @author Saqib Kayani
 *         Date: 24/07/2013
 */
final class G5CompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5CompletedPage.url, G5CompletedPage.title, previousPage){
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G5CompletedPage {
  val title = "Completion - Your Partner"
  val url  = "/yourPartner/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5CompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5CompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G5CompletedPage buildPageWith browser
}
