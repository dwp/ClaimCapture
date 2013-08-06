package xml

import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import xml.XMLHelper._
import scala.xml.NodeSeq

object PayDetails {



  def xml(claim: Claim) = {

    val howWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())
    val bankBuildingSocietyDetails = claim.questionGroup[BankBuildingSocietyDetails].getOrElse(BankBuildingSocietyDetails())

    val showAccount = howWePayYou.paymentFrequency == "01"

    <Payment>
      <PaymentFrequency>{howWePayYou.paymentFrequency}</PaymentFrequency>
      <InitialAccountQuestion>bankBuildingAccount</InitialAccountQuestion>
      {
        if (showAccount){
            <Account>
              <DirectPayment>yes</DirectPayment>
              <AccountHolder>yourName</AccountHolder>
              <HolderName>{bankBuildingSocietyDetails.accountHolderName}</HolderName>
              <SecondHolderName/>
              <AccountType>bank</AccountType>
              <OtherBenefitsToBePaidDirect/>
              <BuildingSocietyDetails>
                <BuildingSocietyQualifier></BuildingSocietyQualifier>
                <AccountNumber>{bankBuildingSocietyDetails.accountNumber}</AccountNumber>
                <RollNumber>{bankBuildingSocietyDetails.rollOrReferenceNumber}</RollNumber>
                <SortCode>{stringify(Some(bankBuildingSocietyDetails.sortCode))}</SortCode>
                <Name>{bankBuildingSocietyDetails.bankFullName}</Name>
                <Branch></Branch>
                <Address>
                  <gds:Line/>
                  <gds:Line/>
                  <gds:Line/>
                  <gds:PostCode/>
                </Address>
                <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
              </BuildingSocietyDetails>
            </Account>
        }else{
          NodeSeq.Empty
        }
      }
    </Payment>
  }

}
