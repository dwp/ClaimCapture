package xml.claim

import models.domain._
import xml.XMLHelper._
import xml.XMLComponent
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

object  Declaration extends XMLComponent {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {
    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    <Declaration>
      <DeclarationStatement>
        <Content>{messagesApi("declaration.openingParagraph")}</Content>
        <Content>{messagesApi("declaration.correct")}</Content>
        <Content>{messagesApi("declaration.overpayment")}</Content>
        <Content>{messagesApi("declaration.reportChanges.pdf")}</Content>
        <Content>{messagesApi("declaration.warning")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"agreement", "Yes")}
    </Declaration>
  }
}
