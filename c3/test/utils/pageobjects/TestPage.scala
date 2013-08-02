package utils.pageobjects

import play.api.test.TestBrowser

/**
 * Represents the test page where the XML "Submitted" is dumped into.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class TestPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, TestPage.url, TestPage.title, previousPage) {
  /**
   * Does nothing.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}

}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object TestPage {
  val title = ""
  val url = "/"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new TestPage(browser, previousPage)
}

/** The context for Specs tests */
trait TestPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = TestPage buildPageWith browser
}