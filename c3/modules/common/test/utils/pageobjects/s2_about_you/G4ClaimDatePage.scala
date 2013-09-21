package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * PageObject for page s2_about_you g4_claimDate.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G4ClaimDatePage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G4ClaimDatePage.url, G4ClaimDatePage.title, previousPage) {
  declareDate("#dateOfClaim", "AboutYouWhenDoYouWantYourCarersAllowanceClaimtoStart")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G4ClaimDatePage {
  val title = "Your claim date - About you - the carer".toLowerCase

  val url  = "/about-you/claim-date"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G4ClaimDatePage(browser,previousPage)
}

/** The context for Specs tests */
trait G4ClaimDatePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4ClaimDatePage (browser)
}