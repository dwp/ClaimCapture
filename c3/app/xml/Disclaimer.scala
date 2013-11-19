package xml

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
        <Content>{Messages("disclaimer.1")}</Content>
        <Content>{Messages("disclaimer.2")}</Content>
        <Content>{Messages("disclaimer.3")}</Content>
        <Content>{Messages("disclaimer.4")}</Content>
        <Content>{Messages("disclaimer.5")}</Content>
        <Content>{Messages("disclaimer.6")}</Content>
        <Content>{Messages("disclaimer.7")}</Content>
      </DisclaimerStatement>

      <DisclaimerQuestion>
        <QuestionLabel>Please tick this box to declare that you have understood the notes and you have made / will make the person you are caring for / or their representative aware that there could be a change to their benefits.</QuestionLabel>
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
