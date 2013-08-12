package utils.pageobjects.S11_consent_and_declaration

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page Object for S10 G5 Submit page.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G5SubmitPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5SubmitPage.url, G5SubmitPage.title, previousPage)

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G5SubmitPage {
  val title = "Submit - Consent And Declaration"

  val url = "/consent-and-declaration/submit"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5SubmitPage(browser, previousPage)
}

/** The context for Specs tests */
trait G5SubmitPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5SubmitPage buildPageWith browser
}