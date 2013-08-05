package utils.pageobjects.S10_consent_and_declaration

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, Page}

/**
 * Page Object for S10 G3 disclaimer.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G3DisclaimerPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3DisclaimerPage.url, G3DisclaimerPage.title, previousPage) {

  declareCheck("#read","ConsentDeclarationDisclaimerTextAndTickBox")

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G3DisclaimerPage {
  val title = "Disclaimer - Consent And Declaration"
  val url = "/consentAndDeclaration/disclaimer"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new G3DisclaimerPage(browser, previousPage)
}

/** The context for Specs tests */
trait G3DisclaimerPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3DisclaimerPage buildPageWith browser
}

