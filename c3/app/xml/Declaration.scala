package xml

import models.domain._
import xml.XMLHelper._
import play.api.i18n.Messages
import scala.Some

object  Declaration {

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

      <DeclarationQuestion>
        <QuestionLabel>{Messages("someoneElse")}</QuestionLabel>
        <Answer>{titleCase(booleanStringToYesNo(declaration.read))}</Answer>
      </DeclarationQuestion>
      <DeclarationQuestion>
        <QuestionLabel>{Messages("confirm")}</QuestionLabel>
        <Answer>{titleCase(booleanStringToYesNo(stringify(declaration.someoneElse)))}</Answer>
      </DeclarationQuestion>

    </Declaration>
  }
}
