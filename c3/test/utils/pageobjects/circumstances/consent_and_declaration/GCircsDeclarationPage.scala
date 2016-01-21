package utils.pageobjects.circumstances.consent_and_declaration

import utils.WithBrowser
import utils.pageobjects._

final class GCircsDeclarationPage (ctx:PageObjectsContext) extends CircumstancesPage(ctx, GCircsDeclarationPage.url){
  declareYesNo("#obtainInfoAgreement","CircumstancesDeclarationInfoAgreement")
  declareInput("#obtainInfoWhy","CircumstancesDeclarationWhyNot")
  declareCheck("#confirm","CircumstancesDeclarationConfirmation")
  declareCheck("#circsSomeOneElse","CircumstancesSomeOneElseConfirmation")
  declareInput("#nameOrOrganisation","NameOrOrganisation")
}

object GCircsDeclarationPage {
  val url  = "/circumstances/consent-and-declaration/declaration"

  def apply(ctx:PageObjectsContext) = new GCircsDeclarationPage(ctx)
}

trait GCircsDeclarationPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = GCircsDeclarationPage(PageObjectsContext(browser))
}


