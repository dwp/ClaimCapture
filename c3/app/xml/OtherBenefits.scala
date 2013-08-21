package xml

import app.XMLValues
import models.domain._
import XMLHelper._
import controllers.Mappings.yes
import controllers.Mappings.no

object OtherBenefits {

  def xml(claim: Claim) = {
    val statutorySickPayOption = claim.questionGroup[StatutorySickPay]
    val otherStatutoryPayOption = claim.questionGroup[OtherStatutoryPay]

    <OtherBenefits>
      <ClaimantBenefits>
        <JobseekersAllowance>no</JobseekersAllowance>
        <IncomeSupport>no</IncomeSupport>
        <PensionCredit>no</PensionCredit>
        <StatePension>no</StatePension>
        <IncapacityBenefit>no</IncapacityBenefit>
        <SevereDisablementAllowance>no</SevereDisablementAllowance>
        <MaternityAllowance>no</MaternityAllowance>
        <UnemployabilitySupplement>no</UnemployabilitySupplement>
        <WindowsBenefit>no</WindowsBenefit>
        <WarWidowsPension>no</WarWidowsPension>
        <IndustrialDeathBenefit>no</IndustrialDeathBenefit>
        <GovernmentTrainingAllowance>no</GovernmentTrainingAllowance>
        <LoneParentChildBenefit>no</LoneParentChildBenefit>
        <OtherSocialSecurityBenefit>{XMLValues.NotAsked}</OtherSocialSecurityBenefit>
        <NonSocialSecurityBenefit>{XMLValues.NotAsked}</NonSocialSecurityBenefit>
        <NoBenefits>{XMLValues.NotAsked}</NoBenefits>
      </ClaimantBenefits>
      <PartnerBenefits>
        <JobseekersAllowance>no</JobseekersAllowance>
        <IncomeSupport>no</IncomeSupport>
        <PensionCredit>no</PensionCredit>
        <StatePension>no</StatePension>
        <IncapacityBenefit>no</IncapacityBenefit>
        <SevereDisablementAllowance>no</SevereDisablementAllowance>
        <MaternityAllowance>no</MaternityAllowance>
        <UnemployabilitySupplement>no</UnemployabilitySupplement>
        <WindowsBenefit>no</WindowsBenefit>
        <WarWidowsPension>no</WarWidowsPension>
        <IndustrialDeathBenefit>no</IndustrialDeathBenefit>
        <GovernmentTrainingAllowance>no</GovernmentTrainingAllowance>
        <OtherSocialSecurityBenefit>{XMLValues.NotAsked}</OtherSocialSecurityBenefit>
        <NonSocialSecurityBenefit>{XMLValues.NotAsked}</NonSocialSecurityBenefit>
        <NoBenefits>{XMLValues.NotAsked}</NoBenefits>
      </PartnerBenefits>
      <ExtraMoney>{XMLValues.NotAsked}</ExtraMoney>
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
        <ConfirmAddress>yes</ConfirmAddress>
      </OtherMoneySMPDetails>
    }
    else <OtherMoneySMP>{otherStatutoryPay.otherPay}</OtherMoneySMP>
  }
}