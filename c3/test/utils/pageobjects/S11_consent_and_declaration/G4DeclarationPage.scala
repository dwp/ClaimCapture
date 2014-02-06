package utils.pageobjects.S11_consent_and_declaration

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G4 declaration.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G4DeclarationPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G4DeclarationPage.url, G4DeclarationPage.title) {
  declareCheck("#someoneElse","ConsentDeclarationSomeoneElseTickBox")
  declareInput("#nameOrOrganisation","ConsentDeclarationNameOrOrganisation")
  declareCheck("#confirm","ConsentDeclarationDeclarationTickBox")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G4DeclarationPage {
  val title = "Declaration - Consent and Declaration".toLowerCase

  val url = "/consent-and-declaration/declaration"

  def apply(ctx:PageObjectsContext) = new G4DeclarationPage(ctx)
}

/** The context for Specs tests */
trait G4DeclarationPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4DeclarationPage (PageObjectsContext(browser))
}