package xml.circumstances

import models.domain.{CircumstancesPaymentChange, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object PaymentChange {
  def xml(circs :Claim): NodeSeq = {
    val circsPaymentChangeOption: Option[CircumstancesPaymentChange] = circs.questionGroup[CircumstancesPaymentChange]

    circsPaymentChangeOption match {
      case Some(circsPaymentChange) => {
        <PaymentChange>
          <PaidIntoAccountDetails>
            {question(<PaidIntoAccount/>,"currentlyPaidIntoBank", circsPaymentChange.currentlyPaidIntoBank)}
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
      <AccountHolder>{bankBuildingSocietyDetails.whoseNameIsTheAccountIn}</AccountHolder>
      <HolderName>{bankBuildingSocietyDetails.accountHolderName}</HolderName>
      <BuildingSocietyDetails>
        <AccountNumber>{bankBuildingSocietyDetails.accountNumber}</AccountNumber>
        {statement(<RollNumber/>,bankBuildingSocietyDetails.rollOrReferenceNumber)}
        {statement(<SortCode/>,bankBuildingSocietyDetails.sortCode)}
        <Name>{bankBuildingSocietyDetails.bankFullName}</Name>
      </BuildingSocietyDetails>
    </AccountDetails>
  }
}
