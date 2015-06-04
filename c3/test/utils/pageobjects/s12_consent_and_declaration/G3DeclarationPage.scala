package utils.pageobjects.s12_consent_and_declaration

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G4 declaration.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G3DeclarationPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G3DeclarationPage.url) {
  declareYesNo("#gettingInformationFromAnyEmployer_informationFromEmployer", "ConsentDeclarationGettingInformationFromAnyEmployer")
  declareInput("#gettingInformationFromAnyEmployer_why", "ConsentDeclarationTellUsWhyEmployer")
  declareYesNo("#tellUsWhyEmployer_informationFromPerson","ConsentDeclarationGettingInformationFromAnyOther")
  declareInput("#tellUsWhyEmployer_whyPerson","ConsentDeclarationTellUsWhyOther")
  declareCheck("#someoneElse","ConsentDeclarationSomeoneElseTickBox")
  declareInput("#nameOrOrganisation","ConsentDeclarationNameOrOrganisation")
  declareCheck("#confirm","ConsentDeclarationDeclarationTickBox")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G3DeclarationPage {
  val url = "/consent-and-declaration/declaration"

  def apply(ctx:PageObjectsContext) = new G3DeclarationPage(ctx)
}

/** The context for Specs tests */
trait G3DeclarationPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3DeclarationPage (PageObjectsContext(browser))
}