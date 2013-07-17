package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject for page s2_about_you g4_claimDate.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class ClaimDatePage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, ClaimDatePage.url, ClaimDatePage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillDate("#dateOfClaim", theClaim.AboutYouWhenDoYouWantYourCarersAllowanceClaimtoStart)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object ClaimDatePage {
  val title = "Claim Date - About You"
  val url  = "/aboutyou/claimDate"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new ClaimDatePage(browser,previousPage)
}

/** The context for Specs tests */
trait ClaimDatePageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = ClaimDatePage buildPageWith browser
}
