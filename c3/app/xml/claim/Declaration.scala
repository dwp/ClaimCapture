package xml.claim

import models.domain._
import xml.XMLHelper._
import play.api.i18n.{MMessages => Messages}
import scala.Some

object  Declaration {

  def xml(claim: Claim) = {

    val additionalInfo = claim.questionGroup[AdditionalInfo].getOrElse(models.domain.AdditionalInfo())
    val consent = claim.questionGroup[Consent].getOrElse(Consent())
    val disclaimer = claim.questionGroup[Disclaimer].getOrElse(Disclaimer())
    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    <Declaration>
      {
        consent.informationFromEmployer.answer match {
          case Some(answer) => <TextLine>Do you agree to us getting information from any current or previous employer you have told us about as part of this claim? {titleCase(answer)}</TextLine>
          case _ =>
        }
      }
      {
        consent.informationFromEmployer.answer.getOrElse("") match {
          case "no" => <TextLine>If you answered No please tell us why</TextLine> <TextLine>{ consent.informationFromEmployer.text.orNull }</TextLine>
          case _ =>
        }
      }

      <TextLine>Do you agree to us getting information from any other person or organisation you have told us about as part of this claim? {titleCase(consent.informationFromPerson.answer)}</TextLine>
      {
      consent.informationFromPerson.answer match {
        case "yes" =>
        case "no" => <TextLine>If you answered No please tell us why</TextLine> <TextLine>{ consent.informationFromPerson.text.orNull }</TextLine>
      }
      }

      <TextLine>This is my claim for Carer's Allowance.</TextLine>
      <TextLine>{Messages("disclaimer.1").replace("[[first name, middle name, surname]]", fullName(claim))}</TextLine>
      <TextLine>{Messages("disclaimer.2").replace("[[first name, middle name, surname]]", fullName(claim))}</TextLine>
      <TextLine>{Messages("disclaimer.3").replace("[[first name, middle name, surname]]", fullName(claim))}</TextLine>
      <TextLine>{Messages("disclaimer.4").replace("[[first name, middle name, surname]]", fullName(claim))}</TextLine>
      <TextLine>{Messages("disclaimer.5").replace("[[first name, middle name, surname]]", fullName(claim))}</TextLine>
      <TextLine>{Messages("disclaimer.6").replace("[[first name, middle name, surname]]", fullName(claim))}</TextLine>
      <TextLine>{Messages("disclaimer.7").replace("[[first name, middle name, surname]]", fullName(claim))}</TextLine>
      <TextLine></TextLine>
      <TextLine>Please tick this box to declare that you have understood the notes and you have made / will make the person you are caring for / or their representative aware that there could be a change to their benefits. = {booleanStringToYesNo(disclaimer.read)}</TextLine>

      <TextLine>{Messages("declaration.1.pdf")}</TextLine>
      <TextLine>{Messages("declaration.2")}</TextLine>
      <TextLine>{Messages("declaration.3")}</TextLine>
      <TextLine>{Messages("declaration.4")}</TextLine>
      <TextLine>We may wish to contact any current or previous employers, or other persons or organisations you have listed on this claim form to obtain information about your claim. You do not have to agree to this but if you do not, it may mean that we are unable to obtain enough information to satisfy ourselves that you meet the conditions of entitlement for your claim.</TextLine>
      <TextLine></TextLine>
      <TextLine>Please tick this box to confirm that you understand and make the declarations above. = {booleanStringToYesNo(declaration.read)}</TextLine>
      <TextLine>Please tick this box if this claim form has been filled in by someone else, if so, please ensure that you understand the declarations above as another person cannot make the declarations on your behalf. = {booleanStringToYesNo(stringify(declaration.someoneElse))}</TextLine>
      {
        declaration.someoneElse match {
          case Some(se) => declaration.nameOrOrganisation match {
            case Some(n) => <TextLine>Your name and/or organisation. = {n}</TextLine>
            case _ =>
          }
          case _ =>
        }
      }

      <TextLine>Do you live in Wales and would like to receive future communications in Welsh? {titleCase(additionalInfo.welshCommunication)}</TextLine>
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
