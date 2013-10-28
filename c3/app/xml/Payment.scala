package xml

import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import xml.XMLHelper._
import scala.xml.NodeSeq
import app.{PaymentFrequency, XMLValues, AccountStatus}
import app.XMLValues._

object Payment {

  def xml(claim: Claim) = {

    val howWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())

    val showAccount = howWePayYou.likeToBePaid == AccountStatus.BankBuildingAccount.name

    <Payment>
      <PaymentFrequency>
        <QuestionLabel>PaymentFrequency?</QuestionLabel>
        <Answer>{howWePayYou.paymentFrequency match {
          case PaymentFrequency.EveryWeek.name => "Weekly"
          case PaymentFrequency.FourWeekly.name => "Four-Weekly"
          case n => "Weekly" // TODO check what the default should be.
        }}</Answer>
      </PaymentFrequency>

      <InitialAccountQuestion>{howWePayYou.likeToBePaid}</InitialAccountQuestion>
      {if (showAccount) account(claim) else NodeSeq.Empty}
    </Payment>
  }

  def account(claim:Claim) = {
    val bankBuildingSocietyDetails = claim.questionGroup[BankBuildingSocietyDetails].getOrElse(BankBuildingSocietyDetails())

    <Account>
      <DirectPayment>{NotAsked}</DirectPayment>
      <AccountHolder>{bankBuildingSocietyDetails.whoseNameIsTheAccountIn}</AccountHolder>
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
        <ConfirmAddress>{yes}</ConfirmAddress>
      </BuildingSocietyDetails>
    </Account>
  }
}