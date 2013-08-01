package xml

import models.domain._
import XMLHelper._
import scala.Some
import controllers.Mappings.yes
import controllers.Mappings.no

object OtherBenefits {

  def xml(otherMoney:Section) = {

    val moneyPaidToSomeoneElseOption = questionGroup[MoneyPaidToSomeoneElseForYou](otherMoney, MoneyPaidToSomeoneElseForYou)
    val personDetailsOption = questionGroup[PersonWhoGetsThisMoney](otherMoney, PersonWhoGetsThisMoney)
    val contactDetailsOption = questionGroup[PersonContactDetails](otherMoney, PersonContactDetails)
    val statutorySickPayOption = questionGroup[StatutorySickPay](otherMoney, StatutorySickPay)
    val otherStatutoryPayOption = questionGroup[OtherStatutoryPay](otherMoney, OtherStatutoryPay)

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
        <OtherSocialSecurityBenefit>no</OtherSocialSecurityBenefit>
        <NonSocialSecurityBenefit>no</NonSocialSecurityBenefit>
        <NoBenefits>yes</NoBenefits>
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
        <OtherSocialSecurityBenefit>no</OtherSocialSecurityBenefit>
        <NonSocialSecurityBenefit>no</NonSocialSecurityBenefit>
        <NoBenefits>yes</NoBenefits>
      </PartnerBenefits>
      {extraMoneyXml(moneyPaidToSomeoneElseOption, personDetailsOption, contactDetailsOption)}
      {otherMoneySPPXml(statutorySickPayOption)}
      {otherMoneySMPXml(otherStatutoryPayOption)}
    </OtherBenefits>
  }

  def extraMoneyXml(moneyPaidToSomeoneElseOption:Option[MoneyPaidToSomeoneElseForYou], personDetailsOption:Option[PersonWhoGetsThisMoney], contactDetailsOption:Option[PersonContactDetails]) = {

    def xml(moneyPaidToSomeoneElseForYou:MoneyPaidToSomeoneElseForYou, personDetails:PersonWhoGetsThisMoney, contactDetails:PersonContactDetails) = {
      if(moneyPaidToSomeoneElseForYou.moneyAddedToBenefitSinceClaimDate == yes) {
        <ExtraMoney>{moneyPaidToSomeoneElseForYou.moneyAddedToBenefitSinceClaimDate}</ExtraMoney>
        <ExtraMoneyDetails>
          <BenefitName>{personDetails.nameOfBenefit}</BenefitName>
          <RecipientName>{personDetails.fullName}</RecipientName>
          <RecipientAddress>{postalAddressStructure(contactDetails.address, contactDetails.postcode)}</RecipientAddress>
          <ConfirmAddress>{yes}</ConfirmAddress>
          <ReferenceNumber>{stringify(personDetails.nationalInsuranceNumber)}</ReferenceNumber>
        </ExtraMoneyDetails>
      }
      else <ExtraMoney>{moneyPaidToSomeoneElseForYou.moneyAddedToBenefitSinceClaimDate}</ExtraMoney>
    }

    val moneyPaidToSomeoneElseForYou = moneyPaidToSomeoneElseOption match {
      case Some(caseClass:MoneyPaidToSomeoneElseForYou) => caseClass
      case _ => MoneyPaidToSomeoneElseForYou(no)
    }

    val personDetails = personDetailsOption match {
      case Some(caseClass:PersonWhoGetsThisMoney) => caseClass
      case _ => PersonWhoGetsThisMoney(fullName = "name", nameOfBenefit= "name")
    }

    val contactDetails = contactDetailsOption match {
      case Some(caseClass:PersonContactDetails) => caseClass
      case _ => PersonContactDetails()
    }

    xml(moneyPaidToSomeoneElseForYou, personDetails, contactDetails)

  }

  def otherMoneySPPXml(statutorySickPayOption:Option[StatutorySickPay]) = {

    def xml(statutorySickPay:StatutorySickPay) = {
      if(statutorySickPay.haveYouHadAnyStatutorySickPay == yes) {
        <OtherMoneySSP>{statutorySickPay.haveYouHadAnyStatutorySickPay}</OtherMoneySSP>
        <OtherMoneySSPDetails>
          <Name>{statutorySickPay.employersName.getOrElse("empty")}</Name>
          <Address>{postalAddressStructure(statutorySickPay.employersAddress, statutorySickPay.employersPostcode)}</Address>
          <ConfirmAddress>{yes}</ConfirmAddress>
        </OtherMoneySSPDetails>
      }
      else <OtherMoneySSP>{statutorySickPay.haveYouHadAnyStatutorySickPay}</OtherMoneySSP>
    }

    statutorySickPayOption match {
      case Some(ssp:StatutorySickPay) => xml(ssp)
      case _ => xml(StatutorySickPay(haveYouHadAnyStatutorySickPay = no))
    }
  }

  def otherMoneySMPXml(otherStatutoryPayOption:Option[OtherStatutoryPay]) = {

    def xml(otherStatutoryPay:OtherStatutoryPay) = {
      if(otherStatutoryPay.otherPay == yes) {
        <OtherMoneySMP>{otherStatutoryPay.otherPay}</OtherMoneySMP>
        <OtherMoneySMPDetails>
          <Name>{otherStatutoryPay.employersName.getOrElse("empty")}</Name>
          <Address>{postalAddressStructure(otherStatutoryPay.employersAddress, otherStatutoryPay.employersPostcode)}</Address>
          <ConfirmAddress>{yes}</ConfirmAddress>
        </OtherMoneySMPDetails>
      }
      else <OtherMoneySMP>{otherStatutoryPay.otherPay}</OtherMoneySMP>
    }

    otherStatutoryPayOption match {
      case Some(osp:OtherStatutoryPay) => xml(osp)
      case _ => xml(OtherStatutoryPay(otherPay = no))
    }
  }
}
