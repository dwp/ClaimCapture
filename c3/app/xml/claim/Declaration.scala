package xml.claim

import models.domain._
import xml.XMLHelper._
import play.api.i18n.Messages
import xml.XMLComponent

object  Declaration extends XMLComponent {

  def xml(claim: Claim) = {
    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    <Declaration>
      <DeclarationStatement>
        <Title>{Messages("declaration.title")}</Title>
        <Content>{Messages("declaration.1.pdf")}</Content>
        <Content>{Messages("declaration.2")}</Content>
        <Content>{Messages("declaration.3")}</Content>
        <Content>{Messages("declaration.4")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"someoneElse", declaration.read)}
      {question(<DeclarationQuestion/>,"confirm", stringify(declaration.someoneElse))}
    </Declaration>
  }
}
