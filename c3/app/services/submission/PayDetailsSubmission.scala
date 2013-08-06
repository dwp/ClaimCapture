package services.submission

import models.domain._
import xml.XMLHelper._
import scala.language.reflectiveCalls

object PayDetailsSubmission {
  def buildPayDetails(claim: Claim) = {
    val howWePayYou: Option[HowWePayYou]          = questionGroup(claim, HowWePayYou)
    val bank : Option[BankBuildingSocietyDetails] = questionGroup(claim, BankBuildingSocietyDetails)

    PayDetails(howWePayYou,bank)
  }

  def buildPayment(payDetails: PayDetails) = {
    <Payment>
      {<PaymentFrequency/> +++ payDetails.howWePayYou.collect{case h:HowWePayYou => h.paymentFrequency}}
      <InitialAccountQuestion>bankBuildingAccount</InitialAccountQuestion>
      {payDetails.howWePayYou.collect{case h:HowWePayYou => h.paymentFrequency} match {
        case Some("01") if payDetails.bankBuildingSocietyDetails.isDefined =>
          <Account>
            <DirectPayment>yes</DirectPayment>
            <AccountHolder>yourName</AccountHolder>
            <HolderName>{payDetails.bankBuildingSocietyDetails.get.accountHolderName}</HolderName>
            <SecondHolderName/>
            <AccountType>bank</AccountType>
            <OtherBenefitsToBePaidDirect/>
            <BuildingSocietyDetails>
              <BuildingSocietyQualifier></BuildingSocietyQualifier>
              <AccountNumber>{payDetails.bankBuildingSocietyDetails.get.accountNumber}</AccountNumber>
              <RollNumber>{payDetails.bankBuildingSocietyDetails.get.rollOrReferenceNumber}</RollNumber>
              <SortCode>{payDetails.bankBuildingSocietyDetails.get.sortCode.fold("error")((s1,s2,s3) => s1+s2+s3)}</SortCode>
              <Name>{payDetails.bankBuildingSocietyDetails.get.bankFullName}</Name>
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
        case _ => {}
       }
      }
    </Payment>
  }

  private def getQuestionGroup[T](claim: Claim, qi: QuestionGroup.Identifier) = {
    claim.questionGroup(qi).asInstanceOf[Option[T]].get
  }

  private def questionGroup[T](claim: Claim, qi: QuestionGroup.Identifier) = {
    claim.questionGroup(qi).asInstanceOf[Option[T]]
  }
}