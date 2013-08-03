package utils.pageobjects.S10_consent_and_declaration

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class G1AdditionalInfoPage  (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1AdditionalInfoPage.url, G1AdditionalInfoPage.title, previousPage) {
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#welshCommunication", theClaim.ConsentDeclarationCommunicationWelsh)
    fillInput("#anythingElse", theClaim.ConsentDeclarationTellUsAnythingElseAboutClaim)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1AdditionalInfoPage {
  val title = "Additional Information - Consent And Declaration"
  val url = "/consentAndDeclaration/additionalInfo"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new G1AdditionalInfoPage(browser, previousPage)
}

/** The context for Specs tests */
trait G1AdditionalInfoPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G1AdditionalInfoPage buildPageWith browser
}