package utils.pageobjects.s_consent_and_declaration

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G4 declaration.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class GDeclarationPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GDeclarationPage.url) {
  declareYesNo("#tellUsWhyFromAnyoneOnForm_informationFromPerson","ConsentDeclarationGettingInformationFromAnyOther")
  declareInput("#tellUsWhyFromAnyoneOnForm_whyPerson","ConsentDeclarationTellUsWhyOther")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object GDeclarationPage {
  val url = "/consent-and-declaration/declaration"

  def apply(ctx:PageObjectsContext) = new GDeclarationPage(ctx)
}

/** The context for Specs tests */
trait GDeclarationPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GDeclarationPage (PageObjectsContext(browser))
}
