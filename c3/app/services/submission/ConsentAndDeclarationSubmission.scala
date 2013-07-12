package services.submission

import models.domain._

object ConsentAndDeclarationSubmission {

  private def getQuestionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]].get
  }
  private def questionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]]
  }

  def buildPayDetails(claim: Claim) = {
    val howWePayYou: HowWePayYou          = getQuestionGroup(claim,HowWePayYou)
    val bank : BankBuildingSocietyDetails = getQuestionGroup(claim,BankBuildingSocietyDetails)

    PayDetails(howWePayYou,bank)
  }

  def buildCaree(payDetails: PayDetails) = {
    <Payment>
      <PaymentFrequency>{payDetails.howWePayYou.paymentFrequency}</PaymentFrequency>
      <InitialAccountQuestion>bankBuildingAccount</InitialAccountQuestion>
      {payDetails.howWePayYou.likeToBePaid match {
        case "01" =>
          <Account>
            <DirectPayment>yes</DirectPayment>
            <AccountHolder>yourName</AccountHolder>
            <HolderName>{payDetails.bankBuildingSocietyDetails.accountHolderName}</HolderName>
            <SecondHolderName/>
            <AccountType>bank</AccountType>
            <OtherBenefitsToBePaidDirect/>
            <BuildingSocietyDetails>
              <BuildingSocietyQualifier></BuildingSocietyQualifier>
              <AccountNumber>{payDetails.bankBuildingSocietyDetails.accountNumber}</AccountNumber>
              <RollNumber>{payDetails.bankBuildingSocietyDetails.rollOrReferenceNumber}</RollNumber>
              <SortCode>{payDetails.bankBuildingSocietyDetails.sortCode.fold("error")((s1,s2,s3) => s1+s2+s3)}</SortCode>
              <Name>{payDetails.bankBuildingSocietyDetails.bankFullName}</Name>
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
}
