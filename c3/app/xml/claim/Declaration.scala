package xml.claim

import models.domain._
import xml.XMLHelper._
import xml.XMLComponent
import play.api.i18n.{MMessages => Messages}

object  Declaration extends XMLComponent {

  def xml(claim: Claim) = {
    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    <Declaration>
      <DeclarationStatement>
        <Title>{Messages("declaration.openingParagraph")}</Title>
        <Content>{Messages("declaration.1")}</Content>
        <Content>{Messages("declaration.2.pdf")}</Content>
        <Content>{Messages("declaration.3.pdf")}</Content>
        <Content>{Messages("declaration.warning")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"someoneElse", declaration.someoneElse)}
      {question(<DeclarationQuestion/>,"confirm", declaration.read)}
      {question(<DeclarationNameOrg/>,"nameOrOrganisation", declaration.nameOrOrganisation)}
    </Declaration>
  }
}
