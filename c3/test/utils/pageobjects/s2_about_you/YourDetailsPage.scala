package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 09/07/2013
 */
class YourDetailsPage(browser: TestBrowser, title: String) extends Page(browser, "/aboutyou/yourDetails", title) {

}

/**
 * Companion object that integrates factory method.
 * It is used by PageBuilder object defined in Page.scala
 */
object YourDetailsPage {
  val title = "Your Details - About You"
  def buildPage(browser: TestBrowser) = new YourDetailsPage(browser, title)
}

/** The context for Specs tests */
trait YourDetailsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = YourDetailsPage buildPage (browser)
}
