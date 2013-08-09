package utils.pageobjects.S11_consent_and_declaration

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page Object for S10 G1 Additional Information.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class G1AdditionalInfoPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1AdditionalInfoPage.url, G1AdditionalInfoPage.title, previousPage) {
 
    declareYesNo("#welshCommunication", "ConsentDeclarationCommunicationWelsh")
    declareInput("#anythingElse", "ConsentDeclarationTellUsAnythingElseAboutClaim")
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
  this: WithBrowser[_] =>
  val page = G1AdditionalInfoPage buildPageWith browser
}