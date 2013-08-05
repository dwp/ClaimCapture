package utils.pageobjects.s3_your_partner

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject for page s2_about_you g8_completed.
 * @author Saqib Kayani
 *         Date: 24/07/2013
 */
final class G5YourPartnerCompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5YourPartnerCompletedPage.url, G5YourPartnerCompletedPage.title, previousPage){

}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G5YourPartnerCompletedPage {
  val title = "Completion - Your Partner"
  val url  = "/yourPartner/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5YourPartnerCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5YourPartnerCompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G5YourPartnerCompletedPage buildPageWith browser
}
