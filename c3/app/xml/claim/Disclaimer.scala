package xml.claim

import models.domain._
import xml.XMLHelper._
import scala.Some
import xml.XMLComponent
import play.api.i18n.{MMessages => Messages}

object  Disclaimer extends XMLComponent{

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
      {question(<DisclaimerQuestion/>,"read", disclaimer.read)}
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
