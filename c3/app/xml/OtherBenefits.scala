package xml

import app.{StatutoryPaymentFrequency, XMLValues}
import app.XMLValues._
import models.domain._
import XMLHelper._


object OtherBenefits {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou]
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay(haveYouHadAnyStatutorySickPay = no))
    val otherStatutoryPayOption = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay(otherPay = no))
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())


    <OtherBenefits>
      <ClaimantBenefits>
        <StatePension>
          <QuestionLabel>StatePension?</QuestionLabel>
          <Answer>{moreAboutYou match {
            case Some(n) => n.receiveStatePension match {
              case "yes" => XMLValues.Yes
              case "no" => XMLValues.No
              case n => n
            }
            case _ => NodeSeq.Empty
          }}</Answer>
        </StatePension>
      </ClaimantBenefits>
      <OtherMoneySSP>
        <QuestionLabel>OtherMoneySSP?</QuestionLabel>
        <Answer>{statutorySickPay.haveYouHadAnyStatutorySickPay match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoneySSP>
      {otherMoneySPPXml(statutorySickPay)}
      <OtherMoneySP>
        <QuestionLabel>OtherMoneySP?</QuestionLabel>
        <Answer>{otherStatutoryPayOption.otherPay match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoneySP>
      {otherMoneySMPXml(otherStatutoryPayOption)}
      <OtherMoney>
        <QuestionLabel>OtherMoney?</QuestionLabel>
        <Answer>{aboutOtherMoney.yourBenefits.answer match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoney>
      <OtherMoneyPayments>
        <QuestionLabel>OtherMoneyPayments?</QuestionLabel>
        <Answer>{aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoneyPayments>


      {aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
          case "yes" =>{
              <OtherMoneyDetails>
                {aboutOtherMoney.whoPaysYou match {
                case Some(n) => {<Name>
                  <QuestionLabel>WhoPaysYou?</QuestionLabel>
                  <Answer>{aboutOtherMoney.whoPaysYou.orNull}</Answer>
                </Name>}
                case None => NodeSeq.Empty
              }}
                <Payment>
                  {aboutOtherMoney.howMuch match {
                  case Some(n) => {
                    <Payment>
                      <QuestionLabel>HowMuch?</QuestionLabel>
                      <Answer>{aboutOtherMoney.howMuch.orNull}</Answer>
                    </Payment>

                  }
                  case None => NodeSeq.Empty
                }}

                {aboutOtherMoney.howOften match {
                  case Some(howOften) => {
                    <Frequency>
                      <QuestionLabel>HowOften?</QuestionLabel>
                      {howOften.frequency match {
                        case n == "Other" => <Other>{n.other}</Other>
                        case _ => NodeSeq.Empty
                      }}
                      <Answer>{StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(aboutOtherMoney.howOften)}</Answer>
                    </Frequency>
                  }
                  case None => NodeSeq.Empty
                }}
                </Payment>
              </OtherMoneyDetails>
          }
          case "no" => NodeSeq.Empty
          case n => throw new RuntimeException("AnyPaymentsSinceClaimDate is either Yes Or No")
        }}
      <EEA>
        <EEAReceivePensionsBenefits>
          <QuestionLabel>EEAReceivePensionsBenefits?</QuestionLabel>
          <Answer>{otherEEAState.benefitsFromOtherEEAStateOrSwitzerland match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </EEAReceivePensionsBenefits>
        <EEAWorkingInsurance>
          <QuestionLabel>EEAWorkingInsurance?</QuestionLabel>
          <Answer>{otherEEAState.workingForOtherEEAStateOrSwitzerland match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </EEAWorkingInsurance>
      </EEA>
    </OtherBenefits>
  }

  def otherMoneySPPXml(statutorySickPay: StatutorySickPay) = {
    if (statutorySickPay.haveYouHadAnyStatutorySickPay == yes) {
      <OtherMoneySSPDetails>
        <Name>{statutorySickPay.employersName.orNull}</Name>
        {postalAddressStructure(statutorySickPay.employersAddress, statutorySickPay.employersPostcode)}
      </OtherMoneySSPDetails>
    }
    else NodeSeq.Empty
  }

  def otherMoneySMPXml(otherStatutoryPay: OtherStatutoryPay) = {
    if (otherStatutoryPay.otherPay == yes) {
      <OtherMoneySPDetails>
        <Name>{otherStatutoryPay.employersName.getOrElse("empty")}</Name>
        {postalAddressStructure(otherStatutoryPay.employersAddress, otherStatutoryPay.employersPostcode)}
      </OtherMoneySPDetails>
    }
    else NodeSeq.Empty
  }
}