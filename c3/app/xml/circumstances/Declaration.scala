package xml.circumstances

import models.domain._
import xml.XMLHelper._
import play.api.i18n.{MMessages => Messages}

object  Declaration {

  def xml(claim: Claim) = {
    val declaration = claim.questionGroup[models.domain.CircumstancesDeclaration].getOrElse(models.domain.CircumstancesDeclaration())

    <Declaration>
      <DeclarationStatement>
        <Content>{Messages("circs.declaration.openingParagraph")}</Content>
        <Content>{Messages("circs.declaration.1")}</Content>
        <Content>{Messages("circs.declaration.2.pdf")}</Content>
        <Content>{Messages("circs.declaration.3.pdf")}</Content>
        <Content>{Messages("circs.declaration.warning")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"circsSomeOneElse", declaration.circsSomeOneElse)}
      {question(<DeclarationQuestion/>,"agreement", "Yes")}
      {question(<DeclarationNameOrg/>,"nameOrOrganisation", declaration.nameOrOrganisation)}
    </Declaration>
  }
}
