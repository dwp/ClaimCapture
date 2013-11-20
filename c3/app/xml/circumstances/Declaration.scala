package xml.circumstances

import models.domain.{Claim, CircumstancesDeclaration}
import scala.xml.NodeSeq
import xml.XMLComponent
import play.api.i18n.Messages
import xml.XMLHelper._

object Declaration  extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
    val declaration = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    <Declaration>
      <DeclarationStatement>
        <Title>{Messages("declaration.title")}</Title>
        <Content>I declare that the information I have given on this form is correct and complete as far as I know and believe.</Content>
        <Content>I understand that if I knowingly give information that is incorrect or incomplete, I may be liable to prosecution or other action.</Content>
        <Content>I understand that I must promptly tell the office that pays my Carer's Allowance of anything that may affect my entitlement to, or the amount of, that benefit.</Content>
      </DeclarationStatement>
      <DeclarationQuestion>
        <QuestionLabel>{Messages("confirm")}</QuestionLabel>
        <Answer>{declaration.confirm}</Answer>
      </DeclarationQuestion>
    </Declaration>
  }
}