package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * * Page object for s5_time_spent_abroad g11_completed.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G12CareYouProvideCompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G12CareYouProvideCompletedPage.url, G12CareYouProvideCompletedPage.title, previousPage)

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G12CareYouProvideCompletedPage {
  val title = "Completion - About the care you provide".toLowerCase

  val url  = "/care-you-provide/completed"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G12CareYouProvideCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G12CareYouProvideCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G12CareYouProvideCompletedPage buildPageWith browser
}