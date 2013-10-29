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

    claim.questionGroup[HowWePayYou] match {
      case Some(howWePayYou) => {
        <Payment>
          <PaymentFrequency>
            <QuestionLabel>PaymentFrequency?</QuestionLabel>
            <Answer>{howWePayYou.paymentFrequency match {
              case PaymentFrequency.EveryWeek.name => "Weekly"
              case PaymentFrequency.FourWeekly.name => "Four-Weekly"
              case n => n // TODO should it throw if type not matched?
            }}</Answer>
          </PaymentFrequency>

          <InitialAccountQuestion>
            <QuestionLabel>InitialAccountQuestion?</QuestionLabel>
            <Answer>{howWePayYou.likeToBePaid}</Answer>
          </InitialAccountQuestion>

          {if (showAccount) account(claim) else NodeSeq.Empty}
        </Payment>
      }
      case None => NodeSeq.Empty
    }
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
      </BuildingSocietyDetails>
    </Account>
  }
}