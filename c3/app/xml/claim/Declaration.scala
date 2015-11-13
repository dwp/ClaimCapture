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
        <Content>{Messages("declaration.openingParagraph")}</Content>
        <Content>{Messages("declaration.correct")}</Content>
        <Content>{Messages("declaration.overpayment")}</Content>
        <Content>{Messages("declaration.reportChanges.pdf")}</Content>
        <Content>{Messages("declaration.warning")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"someoneElse", declaration.someoneElse)}
      {question(<DeclarationQuestion/>,"agreement", "Yes")}
      {question(<DeclarationNameOrg/>,"nameOrOrganisation", declaration.nameOrOrganisation)}
    </Declaration>
  }
}
