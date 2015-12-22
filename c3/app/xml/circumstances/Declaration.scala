package xml.circumstances

import models.domain._
import xml.XMLHelper._
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

object  Declaration {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {
    val declaration = claim.questionGroup[models.domain.CircumstancesDeclaration].getOrElse(models.domain.CircumstancesDeclaration())

    <Declaration>
      <DeclarationStatement>
        <Content>{messagesApi("circs.declaration.openingParagraph")}</Content>
        <Content>{messagesApi("circs.declaration.1")}</Content>
        <Content>{messagesApi("circs.declaration.overpayment")}</Content>
        <Content>{messagesApi("circs.declaration.2.pdf")}</Content>
        <Content>{messagesApi("circs.declaration.warning")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"circsSomeOneElse", declaration.circsSomeOneElse)}
      {question(<DeclarationQuestion/>,"agreement", "Yes")}
      {question(<DeclarationNameOrg/>,"nameOrOrganisation", declaration.nameOrOrganisation)}
    </Declaration>
  }
}
