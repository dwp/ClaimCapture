package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g11_completed.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G12CareYouProvideCompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G12CareYouProvideCompletedPage.url, G12CareYouProvideCompletedPage.title, previousPage) {

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G12CareYouProvideCompletedPage {
  val title = "Completion - About the care you provide"
  val url  = "/careYouProvide/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G12CareYouProvideCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G12CareYouProvideCompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G12CareYouProvideCompletedPage buildPageWith browser
}