package xml.claim

import app.XMLValues._
import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent
import models.domain.Claim
import scala.Some
import models.yesNo.YesNoWithEmployerAndMoney
import play.api.i18n.{MMessages => Messages}


object OtherBenefits extends XMLComponent {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou]
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val statutorySickPay = aboutOtherMoney.statutorySickPay
    val otherStatutoryPayOption = aboutOtherMoney.otherStatutoryPay
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())


    <OtherBenefits>
      <ClaimantBenefits>
        { moreAboutYou match {
            case Some(n) => question(<StatePension/>,"receiveStatePension",n.receiveStatePension)
            case _ => NodeSeq.Empty
          }
        }
      </ClaimantBenefits>
      {question(<OtherMoneySSP/>,"haveYouHadAnyStatutorySickPay.label", statutorySickPay.answer,claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
      {otherMoneySPPXml(statutorySickPay)}
      {question(<OtherMoneySP/>,"otherPay.label",otherStatutoryPayOption.answer,claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))}
      {otherMoneySPDetails(otherStatutoryPayOption)}
      {question(<OtherMoney/>,"yourBenefits.answer", aboutOtherMoney.yourBenefits.answer, if(hadPartnerSinceClaimDate(claim)) Messages("orPartnerSpouse") else "", claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))}
      {question(<OtherMoneyPayments/>,"anyPaymentsSinceClaimDate.answer",aboutOtherMoney.anyPaymentsSinceClaimDate.answer,claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
      {aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
          case "yes" =>{
            <OtherMoneyDetails>
              <Payment>
                {questionCurrency(<Payment/>,"howMuch.label", aboutOtherMoney.howMuch)}
                {questionOther(<Frequency/>,"howOftenPension", aboutOtherMoney.howOften.get.frequency, aboutOtherMoney.howOften.get.other)}
              </Payment>
              {question(<Name/>,"whoPaysYou.label", aboutOtherMoney.whoPaysYou)}
              </OtherMoneyDetails>
          }
          case "no" => NodeSeq.Empty
          case _ => throw new RuntimeException("AnyPaymentsSinceClaimDate is either Yes Or No")
        }}
      <EEA>
        {question(<EEAReceivePensionsBenefits/>,"benefitsFromEEA", otherEEAState.benefitsFromEEA)}
        {question(<EEAClaimPensionsBenefits/>,"claimedForBenefitsFromEEA", otherEEAState.claimedForBenefitsFromEEA)}
        {question(<EEAWorkingInsurance/>,"workingForEEA", otherEEAState.workingForEEA)}
      </EEA>
    </OtherBenefits>
  }

  def otherMoneySPPXml(statutorySickPay: YesNoWithEmployerAndMoney) = {
    if (statutorySickPay.answer.toLowerCase == yes) {
      <OtherMoneySSPDetails>
          <Payment>
            {questionCurrency(<Payment/>,"howMuch", statutorySickPay.howMuch)}
            {questionOther(<Frequency/>,"howOften_frequency", statutorySickPay.howOften.get.frequency, statutorySickPay.howOften.get.other)}
        </Payment>
        {if (statutorySickPay.employersName.isDefined) question(<Name/>, "employersName", statutorySickPay.employersName.get)}
        {postalAddressStructureOpt("employersAddress", statutorySickPay.address, statutorySickPay.postCode)}
      </OtherMoneySSPDetails>
    }
    else NodeSeq.Empty
  }

  def otherMoneySPDetails(otherStatutoryPay: YesNoWithEmployerAndMoney) = {
    if (otherStatutoryPay.answer.toLowerCase == yes) {
      <OtherMoneySPDetails>
          <Payment>
           {questionCurrency(<Payment/>, "howMuch", otherStatutoryPay.howMuch)}
           {questionOther(<Frequency/>,"howOften_frequency", otherStatutoryPay.howOften.get.frequency, otherStatutoryPay.howOften.get.other)}
        </Payment>
        {if (otherStatutoryPay.employersName.isDefined) question(<Name/>, "employersName", otherStatutoryPay.employersName.getOrElse("empty"))}
        {postalAddressStructureOpt("employersAddress", otherStatutoryPay.address, otherStatutoryPay.postCode)}
      </OtherMoneySPDetails>
    }
    else NodeSeq.Empty
  }

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == yes
    case _ => false
  }
}