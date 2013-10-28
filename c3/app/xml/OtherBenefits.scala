package xml

import app.XMLValues
import app.XMLValues._
import models.domain._
import XMLHelper._
import scala.xml.NodeSeq


object OtherBenefits {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou]
    val statutorySickPayOption = claim.questionGroup[StatutorySickPay]
    val otherStatutoryPayOption = claim.questionGroup[OtherStatutoryPay]

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
      <PartnerBenefits>
        <JobseekersAllowance>{no}</JobseekersAllowance>
        <IncomeSupport>{no}</IncomeSupport>
        <PensionCredit>{no}</PensionCredit>
        <StatePension>{no}</StatePension>
        <IncapacityBenefit>{no}</IncapacityBenefit>
        <SevereDisablementAllowance>{no}</SevereDisablementAllowance>
        <MaternityAllowance>{no}</MaternityAllowance>
        <UnemployabilitySupplement>{no}</UnemployabilitySupplement>
        <WindowsBenefit>{no}</WindowsBenefit>
        <WarWidowsPension>{no}</WarWidowsPension>
        <IndustrialDeathBenefit>{no}</IndustrialDeathBenefit>
        <GovernmentTrainingAllowance>{no}</GovernmentTrainingAllowance>
        <OtherSocialSecurityBenefit>{NotAsked}</OtherSocialSecurityBenefit>
        <NonSocialSecurityBenefit>{NotAsked}</NonSocialSecurityBenefit>
        <NoBenefits>{NotAsked}</NoBenefits>
      </PartnerBenefits>
      <ExtraMoney>{NotAsked}</ExtraMoney>
      {otherMoneySPPXml(statutorySickPayOption)}
      {otherMoneySMPXml(otherStatutoryPayOption)}
    </OtherBenefits>
  }

  def otherMoneySPPXml(statutorySickPayOption: Option[StatutorySickPay]) = {

    val statutorySickPay = statutorySickPayOption.getOrElse(StatutorySickPay(haveYouHadAnyStatutorySickPay = no))

    if (statutorySickPay.haveYouHadAnyStatutorySickPay == yes) {
      <OtherMoneySSP>{statutorySickPay.haveYouHadAnyStatutorySickPay}</OtherMoneySSP>
      <OtherMoneySSPDetails>
        <Name>{statutorySickPay.employersName.orNull}</Name>
        <Address>{postalAddressStructure(statutorySickPay.employersAddress, statutorySickPay.employersPostcode)}</Address>
        <ConfirmAddress>yes</ConfirmAddress>
      </OtherMoneySSPDetails>
    }
    else <OtherMoneySSP>{statutorySickPay.haveYouHadAnyStatutorySickPay}</OtherMoneySSP>
  }

  def otherMoneySMPXml(otherStatutoryPayOption: Option[OtherStatutoryPay]) = {

    val otherStatutoryPay = otherStatutoryPayOption.getOrElse(OtherStatutoryPay(otherPay = no))

    if (otherStatutoryPay.otherPay == yes) {
      <OtherMoneySMP>{otherStatutoryPay.otherPay}</OtherMoneySMP>
      <OtherMoneySMPDetails>
        <Name>{otherStatutoryPay.employersName.getOrElse("empty")}</Name>
        <Address>{postalAddressStructure(otherStatutoryPay.employersAddress, otherStatutoryPay.employersPostcode)}</Address>
        <ConfirmAddress>{yes}</ConfirmAddress>
      </OtherMoneySMPDetails>
    }
    else <OtherMoneySMP>{otherStatutoryPay.otherPay}</OtherMoneySMP>
  }
}