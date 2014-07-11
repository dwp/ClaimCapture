package xml.claim

import app.AccountStatus
import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import play.api.i18n.Messages
import xml.XMLComponent
import xml.XMLHelper._

import scala.xml.NodeSeq

object Payment extends XMLComponent {

  def xml(claim: Claim) = {
    val howWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())
    val showAccount = howWePayYou.likeToBePaid == Messages(AccountStatus.BankBuildingAccount)

    claim.questionGroup[HowWePayYou] match {
      case Some(how) => {
        <Payment>
          {question(<PaymentFrequency/>,"paymentFrequency", how.paymentFrequency)}
          {question(<InitialAccountQuestion/>,"likeToPay", how.likeToBePaid)}
          {if (showAccount) account(claim) else NodeSeq.Empty}
        </Payment>
      }
      case None => NodeSeq.Empty
    }
  }

  def account(claim:Claim) = {
    val bankBuildingSocietyDetails = claim.questionGroup[BankBuildingSocietyDetails].getOrElse(BankBuildingSocietyDetails())

    <Account>
      {question(<AccountHolder/>, "whoseNameIsTheAccountIn", bankBuildingSocietyDetails.whoseNameIsTheAccountIn)}
      {question(<HolderName/>, "accountHolderName", encrypt(bankBuildingSocietyDetails.accountHolderName))}
      <BuildingSocietyDetails>
        {question(<AccountNumber/>, "accountNumber", encrypt(bankBuildingSocietyDetails.accountNumber))}
        {question(<RollNumber/>,"rollOrReferenceNumber", bankBuildingSocietyDetails.rollOrReferenceNumber)}
        {question(<SortCode/>,"sortCode", encrypt(bankBuildingSocietyDetails.sortCode))}
        {question(<Name/>, "bankFullName", bankBuildingSocietyDetails.bankFullName)}
      </BuildingSocietyDetails>
    </Account>
  }
}