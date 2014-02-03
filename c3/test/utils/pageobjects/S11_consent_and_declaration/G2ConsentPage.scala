package utils.pageobjects.S11_consent_and_declaration

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10 G2 consent.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G2ConsentPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G2ConsentPage.url, G2ConsentPage.title) {
  declareYesNo("#gettingInformationFromAnyEmployer_informationFromEmployer", "ConsentDeclarationGettingInformationFromAnyEmployer")
  declareInput("#gettingInformationFromAnyEmployer_why", "ConsentDeclarationTellUsWhyEmployer")
  declareYesNo("#tellUsWhyEmployer_informationFromPerson","ConsentDeclarationGettingInformationFromAnyOther")
  declareInput("#tellUsWhyEmployer_whyPerson","ConsentDeclarationTellUsWhyOther")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G2ConsentPage {
  val title = "Consent - Consent and Declaration".toLowerCase

  val url = "/consent-and-declaration/consent"

  def apply(ctx:PageObjectsContext,previousPage: Option[Page] = None) = new G2ConsentPage(ctx)
}

/** The context for Specs tests */
trait G2ConsentPagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2ConsentPage (PageObjectsContext(browser))
}