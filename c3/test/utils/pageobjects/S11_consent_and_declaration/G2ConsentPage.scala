package utils.pageobjects.S11_consent_and_declaration

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page Object for S10 G2 consent.
 * @author Jorge Migueis
 *         Date: 05/08/2013
 */
class G2ConsentPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2ConsentPage.url, G2ConsentPage.title, previousPage) {
  declareYesNo("#informationFromEmployer", "ConsentDeclarationGettingInformationFromAnyEmployer")
  declareInput("#why", "ConsentDeclarationTellUsWhyEmployer")
  declareYesNo("#informationFromPerson","ConsentDeclarationGettingInformationFromAnyOther")
  declareInput("#whyPerson","ConsentDeclarationTellUsWhyOther")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G2ConsentPage {
  val title = "Consent - Consent And Declaration"

  val url = "/consent-and-declaration/consent"

  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new G2ConsentPage(browser, previousPage)
}

/** The context for Specs tests */
trait G2ConsentPagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2ConsentPage buildPageWith browser
}