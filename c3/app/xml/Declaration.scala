package xml

import models.domain._
import xml.XMLHelper._
import play.api.i18n.Messages
import scala.Some

object  Declaration {

  def xml(claim: Claim) = {

    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    <Declaration>

      <!-- : Should be moved to disclaimer
      {Messages("disclaimer.1").replace("[[first name, middle name, surname]]", fullName(claim))}
      {Messages("disclaimer.2").replace("[[first name, middle name, surname]]", fullName(claim))}
      {Messages("disclaimer.3").replace("[[first name, middle name, surname]]", fullName(claim))}
      {Messages("disclaimer.4").replace("[[first name, middle name, surname]]", fullName(claim))}
      {Messages("disclaimer.5").replace("[[first name, middle name, surname]]", fullName(claim))}
      {Messages("disclaimer.6").replace("[[first name, middle name, surname]]", fullName(claim))}
      {Messages("disclaimer.7").replace("[[first name, middle name, surname]]", fullName(claim))}
      
      Please tick this box to declare that you have understood the notes and you have made / will make the person you are caring for / or their representative aware that there could be a change to their benefits. = {booleanStringToYesNo(disclaimer.read)} : Should be moved to disclaimer-->

      <DeclarationStatement>
        <Title>This is my claim for Carer's Allowance.</Title>
        <Content>{Messages("declaration.1.pdf")}</Content>
        <Content>{Messages("declaration.2")}</Content>
        <Content>{Messages("declaration.3")}</Content>
        <Content>{Messages("declaration.4")}</Content>
        <Content>We may wish to contact any current or previous employers, or other persons or organisations you have listed on this claim form to obtain information about your claim. You do not have to agree to this but if you do not, it may mean that we are unable to obtain enough information to satisfy ourselves that you meet the conditions of entitlement for your claim.</Content>
      </DeclarationStatement>

      <DeclarationQuestion>
        <QuestionLabel>Please tick this box to confirm that you understand and make the declarations above.</QuestionLabel>
        <Answer>{titleCase(booleanStringToYesNo(declaration.read))}</Answer>
      </DeclarationQuestion>
      <DeclarationQuestion>
        <QuestionLabel>Please tick this box if this claim form has been filled in by someone else, if so, please ensure that you understand the declarations above as another person cannot make the declarations on your behalf.</QuestionLabel>
        <Answer>{titleCase(booleanStringToYesNo(stringify(declaration.someoneElse)))}</Answer>
      </DeclarationQuestion>

    </Declaration>
  }

  def fullName(claim: Claim) = {
    val personalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())

    personalDetails.middleName match {
      case Some(middleName) => personalDetails.firstName + " " + middleName + " " + personalDetails.surname
      case _ => personalDetails.firstName + " " + personalDetails.surname
    }
  }
}
