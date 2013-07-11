package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 09/07/2013
 */
class YourDetailsPage(browser: TestBrowser) extends Page(browser, "/aboutyou/yourDetails", YourDetailsPage.title) {
  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object YourDetailsPage {
  val title = "Your Details - About You"
  def buildPage(browser: TestBrowser) = new YourDetailsPage(browser)
}

/** The context for Specs tests */
trait YourDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = YourDetailsPage buildPage (browser)
}
