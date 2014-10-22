package xml.circumstances

import models.domain._
import xml.XMLHelper._
import play.api.i18n.{MMessages => Messages}

object  Declaration {

  def xml(claim: Claim) = {
    val declaration = claim.questionGroup[models.domain.CircumstancesDeclaration].getOrElse(models.domain.CircumstancesDeclaration())

    <Declaration>
      <DeclarationStatement>
        <Title>{Messages("declaration.title")}</Title>
        <Content>{Messages("declaration.1.pdf")}</Content>
        <Content>{Messages("declaration.2")}</Content>
        <Content>{Messages("declaration.3")}</Content>
        <Content>{Messages("declaration.4")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"circsSomeOneElse", declaration.circsSomeOneElse)}
      {question(<DeclarationQuestion/>,"confirm", declaration.confirm)}
      {question(<DeclarationNameOrg/>,"nameOrOrganisation", declaration.nameOrOrganisation)}
    </Declaration>
  }
}
