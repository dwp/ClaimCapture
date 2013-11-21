package xml.claim

import app.{StatutoryPaymentFrequency, XMLValues}
import app.XMLValues._
import models.domain._
import scala.xml.{Node, NodeSeq}
import xml.XMLHelper._
import models.domain.Claim
import scala.Some
import xml.XMLComponent
import models.domain.Claim
import scala.Some
import play.api.i18n.Messages


object OtherBenefits extends XMLComponent {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou]
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay(haveYouHadAnyStatutorySickPay = no))
    val otherStatutoryPayOption = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay(otherPay = no))
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())


    <OtherBenefits>
      <ClaimantBenefits>
          {
          moreAboutYou match {
            case Some(n) => n.receiveStatePension.isEmpty match {
              case false => {
              val parentNode = <StatePension></StatePension>
                  optionalQuestions(n.receiveStatePension,parentNode,question("receiveStatePension", n.receiveStatePension))
               }
              case _ => NodeSeq.Empty
            }
            case _ => NodeSeq.Empty
          }
        }
      </ClaimantBenefits>
      {val parentNode = <OtherMoneySSP/>
       optionalQuestions(statutorySickPay.haveYouHadAnyStatutorySickPay,parentNode,question("haveYouHadAnyStatutorySickPay.label", statutorySickPay.haveYouHadAnyStatutorySickPay))
      }
      {otherMoneySPPXml(statutorySickPay)}
      <OtherMoneySP>
        <QuestionLabel>OtherMoneySP?</QuestionLabel>
        <Answer>{otherStatutoryPayOption.otherPay match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoneySP>
      {otherMoneySPDetails(otherStatutoryPayOption)}
      <OtherMoney>
        <QuestionLabel>OtherMoney?</QuestionLabel>
        <Answer>{aboutOtherMoney.yourBenefits.answer match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoney>

      {
      <OtherMoneyPayments>
        <QuestionLabel>OtherMoneyPayments?</QuestionLabel>
        <Answer>{aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </OtherMoneyPayments>
      }

      {aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
          case "yes" =>{
              <OtherMoneyDetails>
                <Payment>
                  {aboutOtherMoney.howMuch match {
                  case Some(n) => {
                    <Payment>
                      <QuestionLabel>HowMuch?</QuestionLabel>
                      <Answer>
                        <Currency>{GBP}</Currency>
                        <Amount>{aboutOtherMoney.howMuch.orNull}</Amount>
                      </Answer>
                    </Payment>

                  }
                  case None => NodeSeq.Empty
                }}

                {aboutOtherMoney.howOften match {
                  case Some(howOften) => {
                    <Frequency>
                      <QuestionLabel>HowOften?</QuestionLabel>
                      {howOften.frequency match {
                        case "Other" => <Other>{howOften.other.orNull}</Other>
                        case _ => NodeSeq.Empty
                      }}
                      <Answer>{StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(aboutOtherMoney.howOften)}</Answer>
                    </Frequency>
                  }
                  case None => NodeSeq.Empty
                }}
                </Payment>

                {aboutOtherMoney.whoPaysYou match {
                  case Some(n) => {<Name>
                    <QuestionLabel>WhoPaysYou?</QuestionLabel>
                    <Answer>{aboutOtherMoney.whoPaysYou.orNull}</Answer>
                  </Name>}
                  case None => NodeSeq.Empty
                }}

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
          <Payment>
            {statutorySickPay.howMuch match {
            case Some(n) => {
              {val parentNode = <Payment/>
                optionalQuestions(statutorySickPay.howMuch.orNull,parentNode,questionCurrency("howMuch", statutorySickPay.howMuch.orNull))
              }
            }
            case None => NodeSeq.Empty
          }}

          {statutorySickPay.howOften match {
          case Some(howOften) => {
            <Frequency>
              <QuestionLabel>{Messages("howOften_frequency")}</QuestionLabel>
              {howOften.frequency match {
              case "Other" => <Other>{howOften.other.getOrElse("")}</Other>
              case _ => NodeSeq.Empty
            }}
              <Answer>{StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(statutorySickPay.howOften)}</Answer>
            </Frequency>
          }
          case None => NodeSeq.Empty
        }}
        </Payment>
        <Name>{statutorySickPay.employersName.orNull}</Name>
        {postalAddressStructure(statutorySickPay.employersAddress, statutorySickPay.employersPostcode)}
      </OtherMoneySSPDetails>
    }
    else NodeSeq.Empty
  }

  def otherMoneySPDetails(otherStatutoryPay: OtherStatutoryPay) = {
    if (otherStatutoryPay.otherPay == yes) {
      <OtherMoneySPDetails>
          <Payment>
            {otherStatutoryPay.howMuch match {
            case Some(n) => {
              <Payment>
                <QuestionLabel>HowMuchSP?</QuestionLabel>
                <Answer>
                  <Currency>{GBP}</Currency>
                  <Amount>{otherStatutoryPay.howMuch.orNull}</Amount>
                </Answer>
              </Payment>

            }
            case None => NodeSeq.Empty
          }}

          {otherStatutoryPay.howOften match {
          case Some(howOften) => {
            <Frequency>
              <QuestionLabel>HowOftenSP?</QuestionLabel>
              {howOften.frequency match {
              case "Other" => <Other>{howOften.other.getOrElse("")}</Other>
              case _ => NodeSeq.Empty
            }}
              <Answer>{StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(otherStatutoryPay.howOften)}</Answer>
            </Frequency>
          }
          case None => NodeSeq.Empty
        }}
        </Payment>
        <Name>{otherStatutoryPay.employersName.getOrElse("empty")}</Name>
        {postalAddressStructure(otherStatutoryPay.employersAddress, otherStatutoryPay.employersPostcode)}
      </OtherMoneySPDetails>
    }
    else NodeSeq.Empty
  }
}