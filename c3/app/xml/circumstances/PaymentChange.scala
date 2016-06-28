package xml.circumstances

import models.domain.{CircumstancesPaymentChange, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

object PaymentChange {
  def xml(circs :Claim): NodeSeq = {
    val circsPaymentChangeOption: Option[CircumstancesPaymentChange] = circs.questionGroup[CircumstancesPaymentChange]

    circsPaymentChangeOption match {
      case Some(circsPaymentChange) => {
        <PaymentChange>
          <PaidIntoAccountDetails>
            {question(<PaidIntoAccount/>,"currentlyPaidIntoBank.label", circsPaymentChange.currentlyPaidIntoBank.answer)}
            {question(<BankName/>,"currentlyPaidIntoBank.text1", circsPaymentChange.currentlyPaidIntoBank.text1)}
            {question(<MethodOfPayment/>,"currentlyPaidIntoBank.text2", circsPaymentChange.currentlyPaidIntoBank.text2)}
          </PaidIntoAccountDetails>
          {account(circs)}
          {question(<PaymentFrequency/>,"paymentFrequency", circsPaymentChange.paymentFrequency)}
          {question(<OtherChanges/>, "moreAboutChanges", circsPaymentChange.moreAboutChanges)}
        </PaymentChange>
      }
      case _ => NodeSeq.Empty
    }
  }

  def account(circs:Claim) = {
    val bankBuildingSocietyDetails = circs.questionGroup[CircumstancesPaymentChange].getOrElse(CircumstancesPaymentChange())
    <AccountDetails>
      {question(<HolderName/>, "accountHolderName", encrypt(bankBuildingSocietyDetails.accountHolderName))}
      <BuildingSocietyDetails>
        {question(<AccountNumber/>, "accountNumber", encrypt(bankBuildingSocietyDetails.accountNumber.replaceAll(" ","")))}
        {question(<RollNumber/>,"rollOrReferenceNumber", bankBuildingSocietyDetails.rollOrReferenceNumber)}
        {question(<SortCode/>,"sortCode", encrypt(bankBuildingSocietyDetails.sortCode))}
        {question(<Name/>, "bankFullName", bankBuildingSocietyDetails.bankFullName)}
      </BuildingSocietyDetails>
    </AccountDetails>
  }
}
