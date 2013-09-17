package utils.pageobjects.circumstances.s3_consent_and_declaration

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{CircumstancesPage, PageContext, Page}

final class G1DeclarationPage (browser: TestBrowser, previousPage: Option[Page] = None) extends CircumstancesPage(browser, G1DeclarationPage.url, G1DeclarationPage.title, previousPage){
  declareYesNo("#obtainInfoAgreement","CircumstancesDeclarationInfoAgreement")
  declareInput("#obtainInfoWhy","CircumstancesDeclarationWhyNot")
  declareCheck("#confirm","CircumstancesDeclarationConfirmation")
}

object G1DeclarationPage {
  val title = "Declaration - consent and declaration".toLowerCase

  val url  = "/circumstances/consent-and-declaration/declaration"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1DeclarationPage(browser, previousPage)
}

trait G1DeclarationPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G1DeclarationPage buildPageWith browser
}


