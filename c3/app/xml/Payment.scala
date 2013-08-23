package xml

import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import xml.XMLHelper._
import scala.xml.NodeSeq
import scala.Some
import app.{XMLValues, AccountStatus}

object Payment {

  def xml(claim: Claim) = {

    val howWePayYou = claim.questionGroup[HowWePayYou].get //OrElse(HowWePayYou())

    val showAccount = howWePayYou.likeToBePaid == AccountStatus.BankBuildingAccount.name

    <Payment>
      <PaymentFrequency>{howWePayYou.paymentFrequency}</PaymentFrequency>
      <InitialAccountQuestion>{howWePayYou.likeToBePaid}</InitialAccountQuestion>
      {if (showAccount) account(claim) else NodeSeq.Empty}
    </Payment>
  }

  def account(claim:Claim) = {
    val bankBuildingSocietyDetails = claim.questionGroup[BankBuildingSocietyDetails].getOrElse(BankBuildingSocietyDetails())

    <Account>
      <DirectPayment>{XMLValues.NotAsked}</DirectPayment>
      <AccountHolder>{XMLValues.NotAsked}</AccountHolder>
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
