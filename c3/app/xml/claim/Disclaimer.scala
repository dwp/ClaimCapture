package xml.claim

import models.domain._
import xml.XMLHelper._
import play.api.i18n.Messages
import scala.Some

object  Disclaimer{

  def xml(claim: Claim) = {

    val disclaimer = claim.questionGroup[models.domain.Disclaimer].getOrElse(models.domain.Disclaimer())

    <Disclaimer>

      <DisclaimerStatement>
        <Title>This is my claim for Carer's Allowance.</Title>
        <Content>{Messages("disclaimer.1").replaceAllLiterally("[first name, middle name, surname]", fullName(claim))}</Content>
        <Content>{Messages("disclaimer.2")}</Content>
        <Content>{Messages("disclaimer.3").replaceAllLiterally("[first name, middle name, surname]", fullName(claim))}</Content>
        <Content>{Messages("disclaimer.4").replaceAllLiterally("[first name, middle name, surname]", fullName(claim))}</Content>
        <Content>{Messages("disclaimer.5").replaceAllLiterally("[first name, middle name, surname]", fullName(claim))}</Content>
        <Content>{Messages("disclaimer.6").replaceAllLiterally("[first name, middle name, surname]", fullName(claim))}</Content>
        <Content>{Messages("disclaimer.7").replaceAllLiterally("[first name, middle name, surname]", fullName(claim))}</Content>
      </DisclaimerStatement>

      <DisclaimerQuestion>
        <QuestionLabel>{Messages("read")}</QuestionLabel>
        <Answer>{titleCase(booleanStringToYesNo(disclaimer.read))}</Answer>
      </DisclaimerQuestion>

    </Disclaimer>
  }

  def fullName(claim: Claim) = {
    val personalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())

    personalDetails.middleName match {
      case Some(middleName) => personalDetails.firstName + " " + middleName + " " + personalDetails.surname
      case _ => personalDetails.firstName + " " + personalDetails.surname
    }
  }
}
