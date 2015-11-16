package xml.claim

import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import xml.XMLComponent
import xml.XMLHelper._
import scala.xml.NodeSeq
import play.api.i18n.Messages
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

object Payment extends XMLComponent {

  def xml(claim: Claim) = {
    val howWePayYou: HowWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())
    val showAccount = howWePayYou.likeToBePaid == "yes"

    claim.questionGroup[HowWePayYou] match {
      case Some(how) => {
        <Payment>
          {question(<PaymentFrequency/>,"paymentFrequency", how.paymentFrequency)}
          {question(<InitialAccountQuestion/>,"likeToPay", how.likeToBePaid)}
          {if (showAccount) account(howWePayYou) else NodeSeq.Empty}
        </Payment>
      }
      case None => NodeSeq.Empty
    }
  }

  def account(howWePayYou:HowWePayYou) = {
    val bankBuildingSocietyDetails = howWePayYou.bankDetails.getOrElse(BankBuildingSocietyDetails())

    <Account>
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
