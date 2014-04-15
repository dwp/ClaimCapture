package xml.claim

import app.XMLValues._
import models.domain._
import xml.XMLHelper._


object OtherBenefits {

  def xml(claim: Claim) = {
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney]

    <OtherBenefits>
      <ClaimantBenefits>
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
        <LoneParentChildBenefit>{no}</LoneParentChildBenefit>
        <OtherSocialSecurityBenefit>{NotAsked}</OtherSocialSecurityBenefit>
        <NonSocialSecurityBenefit>{NotAsked}</NonSocialSecurityBenefit>
        <NoBenefits>{NotAsked}</NoBenefits>
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
      {otherMoneySPPXml(aboutOtherMoney)}
      {otherMoneySMPXml(aboutOtherMoney)}
    </OtherBenefits>
  }

  def otherMoneySPPXml(aboutOtherMoney: Option[AboutOtherMoney]) = {

    val statutorySickPay = aboutOtherMoney.getOrElse(AboutOtherMoney())

    if (statutorySickPay.statutorySickPay.answer == yes) {
      <OtherMoneySSP>{statutorySickPay.statutorySickPay.answer}</OtherMoneySSP>
      <OtherMoneySSPDetails>
        <Name>{statutorySickPay.statutorySickPay.employersName.orNull}</Name>
        <Address>{postalAddressStructure(statutorySickPay.statutorySickPay.address, statutorySickPay.statutorySickPay.postCode)}</Address>
        <ConfirmAddress>yes</ConfirmAddress>
      </OtherMoneySSPDetails>
    }
    else <OtherMoneySSP>{statutorySickPay.statutorySickPay.answer}</OtherMoneySSP>
  }

  def otherMoneySMPXml(aboutOtherMoney: Option[AboutOtherMoney]) = {

    val otherStatutoryPay = aboutOtherMoney.getOrElse(AboutOtherMoney())

    if (otherStatutoryPay.otherStatutoryPay.answer == yes) {
      <OtherMoneySMP>{otherStatutoryPay.otherStatutoryPay.answer}</OtherMoneySMP>
      <OtherMoneySMPDetails>
        <Name>{otherStatutoryPay.otherStatutoryPay.employersName.getOrElse("empty")}</Name>
        <Address>{postalAddressStructure(otherStatutoryPay.otherStatutoryPay.address, otherStatutoryPay.otherStatutoryPay.postCode)}</Address>
        <ConfirmAddress>{yes}</ConfirmAddress>
      </OtherMoneySMPDetails>
    }
    else <OtherMoneySMP>{otherStatutoryPay.otherStatutoryPay.answer}</OtherMoneySMP>
  }
}