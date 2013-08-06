package xml

import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import xml.XMLHelper._
import scala.xml.NodeSeq
import scala.Some

object PayDetails {

  def xml(claim: Claim) = {

    val howWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())
    val showAccount = howWePayYou.paymentFrequency == "01"

    <Payment>
      <PaymentFrequency>{howWePayYou.paymentFrequency}</PaymentFrequency>
      <InitialAccountQuestion>bankBuildingAccount</InitialAccountQuestion>
      {if (showAccount) account(claim) else NodeSeq.Empty}
    </Payment>
  }

  def account(claim:Claim) = {
    val bankBuildingSocietyDetails = claim.questionGroup[BankBuildingSocietyDetails].getOrElse(BankBuildingSocietyDetails())

    <Account>
      <DirectPayment>yes</DirectPayment>
      <AccountHolder>yourName</AccountHolder>
      <HolderName>{bankBuildingSocietyDetails.accountHolderName}</HolderName>
      <SecondHolderName/>
      <AccountType>bank</AccountType>
      <OtherBenefitsToBePaidDirect/>
      <BuildingSocietyDetails>
        <BuildingSocietyQualifier/>
        <AccountNumber>{bankBuildingSocietyDetails.accountNumber}</AccountNumber>
        <RollNumber>{bankBuildingSocietyDetails.rollOrReferenceNumber}</RollNumber>
        <SortCode>{stringify(Some(bankBuildingSocietyDetails.sortCode))}</SortCode>
        <Name>{bankBuildingSocietyDetails.bankFullName}</Name>
        <Branch></Branch>
        <Address>{postalAddressStructure(None, None)}</Address>
        <ConfirmAddress>yes</ConfirmAddress>
      </BuildingSocietyDetails>
    </Account>
  }

}
