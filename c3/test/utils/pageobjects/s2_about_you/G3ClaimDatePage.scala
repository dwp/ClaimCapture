package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * PageObject for page s2_about_you g3_claimDate.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G3ClaimDatePage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G3ClaimDatePage.url, G3ClaimDatePage.title, previousPage) {
  declareDate("#dateOfClaim", "AboutYouWhenDoYouWantYourCarersAllowanceClaimtoStart")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G3ClaimDatePage {
  val title = "Your claim date - About you - the carer".toLowerCase

  val url  = "/about-you/claim-date"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G3ClaimDatePage(browser,previousPage)
}

/** The context for Specs tests */
trait G3ClaimDatePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3ClaimDatePage (browser)
}