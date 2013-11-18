package xml

import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import xml.XMLHelper._
import scala.xml.NodeSeq
import app.{PaymentFrequency, XMLValues, AccountStatus}
import app.XMLValues._
import play.api.i18n.Messages

object Payment {

  def xml(claim: Claim) = {

    val howWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())

    val showAccount = howWePayYou.likeToBePaid == Messages(AccountStatus.BankBuildingAccount.name)

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
            <Answer>{howWePayYou.likeToBePaid match {
              case AccountStatus.BankBuildingAccount.name => Messages(AccountStatus.BankBuildingAccount.name)
              case AccountStatus.AppliedForAccount.name => Messages(AccountStatus.AppliedForAccount.name)
              case AccountStatus.NotOpenAccount.name => Messages(AccountStatus.NotOpenAccount.name)
              case n => n
              }
            }</Answer>
          </InitialAccountQuestion>

          {if (showAccount) account(claim) else NodeSeq.Empty}
        </Payment>
      }
      case None => NodeSeq.Empty
    }
  }

  def account(claim:Claim) = {
    val bankBuildingSocietyDetails = claim.questionGroup[BankBuildingSocietyDetails].getOrElse(BankBuildingSocietyDetails())


    // TODO If fields below are no longer in the schema then they don't need to be on the website e.g. AccountHolder
    <Account>
      <!--<AccountHolder>{bankBuildingSocietyDetails.whoseNameIsTheAccountIn}</AccountHolder>-->
      <HolderName>{bankBuildingSocietyDetails.accountHolderName}</HolderName>
      <BuildingSocietyDetails>
        <AccountNumber>{bankBuildingSocietyDetails.accountNumber}</AccountNumber>

        {if(bankBuildingSocietyDetails.rollOrReferenceNumber.isEmpty) NodeSeq.Empty
        else <RollNumber>{bankBuildingSocietyDetails.rollOrReferenceNumber}</RollNumber>}

        <SortCode>{stringify(Some(bankBuildingSocietyDetails.sortCode))}</SortCode>
        <Name>{bankBuildingSocietyDetails.bankFullName}</Name>
      </BuildingSocietyDetails>
    </Account>
  }
}