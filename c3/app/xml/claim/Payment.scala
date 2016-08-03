package xml.claim

import models.SortCode
import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import xml.XMLComponent
import xml.XMLHelper._
import scala.xml.NodeSeq

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
        {question(<AccountNumber/>, "accountNumber", encrypt(bankBuildingSocietyDetails.accountNumber.replaceAll(" ","")))}
        {question(<RollNumber/>,"rollOrReferenceNumber", bankBuildingSocietyDetails.rollOrReferenceNumber)}
        {question(<SortCode/>,"sortCode", encrypt(bankBuildingSocietyDetails.sortCode))}
        {question(<Name/>, "bankFullName", bankBuildingSocietyDetails.bankFullName)}
      </BuildingSocietyDetails>
    </Account>
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim.update(createHowWePayYouFromXml(xml))
  }

  private def createHowWePayYouFromXml(xml: NodeSeq) = {
    val payment = (xml \\ "Payment")
    models.domain.HowWePayYou(
      likeToBePaid = createYesNoText((payment \ "InitialAccountQuestion" \ "Answer").text),
      paymentFrequency = (payment \ "PaymentFrequency" \ "Answer").text,
      bankDetails = createBankDetailsOptionalFromXml(xml)
    )
  }

  private def createBankDetailsOptionalFromXml(xml: NodeSeq) = {
    val account = (xml \\ "Account")
    account.isEmpty match {
      case false =>
        Some(models.domain.BankBuildingSocietyDetails (
          accountHolderName = decrypt((account \ "HolderName" \ "Answer").text),
          accountNumber = decrypt((account \ "BuildingSocietyDetails" \ "AccountNumber" \ "Answer").text),
          rollOrReferenceNumber = (account \ "BuildingSocietyDetails" \ "RollNumber" \ "Answer").text,
          sortCode = createSortCode(decrypt((account \ "BuildingSocietyDetails" \ "SortCode" \ "Answer").text)),
          bankFullName = (account \ "BuildingSocietyDetails" \ "Name" \ "Answer").text
        ))
      case true => None
    }
  }

  private def createSortCode(sortCode: String): SortCode = {
    new SortCode(sort1 = sortCode.take(2), sort2 = sortCode.drop(2).take(2), sort3 = sortCode.drop(4).take(2))
  }
}
