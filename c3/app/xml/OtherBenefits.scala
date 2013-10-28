package xml

import app.XMLValues
import app.XMLValues._
import models.domain._
import XMLHelper._
import scala.xml.NodeSeq


object OtherBenefits {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou]
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay(haveYouHadAnyStatutorySickPay = no))
    val otherStatutoryPayOption = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay(otherPay = no))


    <OtherBenefits>
      <ClaimantBenefits>
        <StatePension>
          <QuestionLabel>StatePension?</QuestionLabel>
          <Answer>{moreAboutYou match {
            case Some(n) => n.receiveStatePension match {
              case "yes" => XMLValues.Yes
              case "no" => XMLValues.No
              case n => n
            }
            case _ => NodeSeq.Empty
          }}</Answer>
        </StatePension>
      </ClaimantBenefits>
      <OtherMoneySSP>
        <QuestionLabel>OtherMoneySSP?</QuestionLabel>
        <Answer>{statutorySickPay.haveYouHadAnyStatutorySickPay match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoneySSP>
      {otherMoneySPPXml(statutorySickPay)}
      <OtherMoneySP>
        <QuestionLabel>OtherMoneySP?</QuestionLabel>
        <Answer>{otherStatutoryPayOption.otherPay match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoneySP>
      {otherMoneySMPXml(otherStatutoryPayOption)}
    </OtherBenefits>
  }

  def otherMoneySPPXml(statutorySickPay: StatutorySickPay) = {
    if (statutorySickPay.haveYouHadAnyStatutorySickPay == yes) {
      <OtherMoneySSPDetails>
        <Name>{statutorySickPay.employersName.orNull}</Name>
        <Address>{postalAddressStructure(statutorySickPay.employersAddress, statutorySickPay.employersPostcode)}</Address>
        <ConfirmAddress>yes</ConfirmAddress>
      </OtherMoneySSPDetails>
    }
    else <OtherMoneySSP>{statutorySickPay.haveYouHadAnyStatutorySickPay}</OtherMoneySSP>
  }

  def otherMoneySMPXml(otherStatutoryPay: OtherStatutoryPay) = {
    if (otherStatutoryPay.otherPay == yes) {
      <OtherMoneySMPDetails>
        <Name>{otherStatutoryPay.employersName.getOrElse("empty")}</Name>
        <Address>{postalAddressStructure(otherStatutoryPay.employersAddress, otherStatutoryPay.employersPostcode)}</Address>
        <ConfirmAddress>{yes}</ConfirmAddress>
      </OtherMoneySMPDetails>
    }
    else <OtherMoneySMP>{otherStatutoryPay.otherPay}</OtherMoneySMP>
  }
}