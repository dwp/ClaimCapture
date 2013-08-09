package utils.pageobjects.S11_consent_and_declaration

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page Object for S10 G4 declaration.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G4DeclarationPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4DeclarationPage.url, G4DeclarationPage.title, previousPage) {

  declareCheck("#someoneElse","ConsentDeclarationSomeoneElseTickBox")
  declareCheck("#confirm","ConsentDeclarationDeclarationTickBox")
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G4DeclarationPage {
  val title = "Declaration - Consent And Declaration"
  val url = "/consentAndDeclaration/declaration"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new G4DeclarationPage(browser, previousPage)
}

/** The context for Specs tests */
trait G4DeclarationPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G4DeclarationPage buildPageWith browser
}