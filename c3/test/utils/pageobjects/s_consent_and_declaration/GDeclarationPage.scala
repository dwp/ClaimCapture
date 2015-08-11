package utils.pageobjects.s_consent_and_declaration

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G4 declaration.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class GDeclarationPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GDeclarationPage.url) {
  declareYesNo("#gettingInformationFromAnyEmployer_informationFromEmployer", "ConsentDeclarationGettingInformationFromAnyEmployer")
  declareInput("#gettingInformationFromAnyEmployer_why", "ConsentDeclarationTellUsWhyEmployer")
  declareYesNo("#tellUsWhyEmployer_informationFromPerson","ConsentDeclarationGettingInformationFromAnyOther")
  declareInput("#tellUsWhyEmployer_whyPerson","ConsentDeclarationTellUsWhyOther")
  declareCheck("#someoneElse","ConsentDeclarationSomeoneElseTickBox")
  declareInput("#nameOrOrganisation","ConsentDeclarationNameOrOrganisation")
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