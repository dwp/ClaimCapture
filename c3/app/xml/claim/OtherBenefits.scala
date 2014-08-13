package xml.claim

import app.XMLValues._
import models.domain._
import models.yesNo.YesNoWithEmployerAndMoney
import play.api.i18n.{MMessages => Messages}
import xml.XMLComponent
import xml.XMLHelper._

import scala.xml.NodeSeq


object OtherBenefits extends XMLComponent {

  def xml(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val statutorySickPay = aboutOtherMoney.statutorySickPay
    val otherStatutoryPayOption = aboutOtherMoney.otherStatutoryPay
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())


    <OtherBenefits>
      <ClaimantBenefits>
        {question(<StatePension/>,"receiveStatePension",yourDetails.receiveStatePension)}
      </ClaimantBenefits>
      {question(<OtherMoneySSP/>,"haveYouHadAnyStatutorySickPay.label", statutorySickPay.answer,claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
      {otherMoneySPPXml(statutorySickPay)}
      {question(<OtherMoneySP/>,"otherPay.label",otherStatutoryPayOption.answer,claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))}
      {otherMoneySPDetails(otherStatutoryPayOption)}
      {question(<OtherMoneyPayments/>,"anyPaymentsSinceClaimDate.answer",aboutOtherMoney.anyPaymentsSinceClaimDate.answer,claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))}
      {aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
          case "yes" =>{
            <OtherMoneyDetails>
              <Payment>
                {questionCurrency(<Payment/>,"howMuch.label", aboutOtherMoney.howMuch)}
                {if(aboutOtherMoney.howOften.isDefined){
                  {questionOther(<Frequency/>,"howOftenPension", aboutOtherMoney.howOften.get.frequency, aboutOtherMoney.howOften.get.other)}
                }}
              </Payment>
              {question(<Name/>,"whoPaysYou.label", aboutOtherMoney.whoPaysYou)}
              </OtherMoneyDetails>
          }
          case "no" => NodeSeq.Empty
          case _ => throw new RuntimeException("AnyPaymentsSinceClaimDate is either Yes Or No")
        }}
      <EEA>
        {question(<EEAReceivePensionsBenefits/>,"benefitsFromEEA", otherEEAState.benefitsFromEEA)}
        {question(<EEAWorkingInsurance/>,"workingForEEA", otherEEAState.workingForEEA)}
      </EEA>
    </OtherBenefits>
  }

  def otherMoneySPPXml(statutorySickPay: YesNoWithEmployerAndMoney) = {
    if (statutorySickPay.answer.toLowerCase == yes) {
      <OtherMoneySSPDetails>
          <Payment>
            {questionCurrency(<Payment/>,"howMuch", statutorySickPay.howMuch)}
            {if (statutorySickPay.howOften.isDefined){
              questionOther(<Frequency/>,"howOften_frequency", statutorySickPay.howOften.get.frequency, statutorySickPay.howOften.get.other)
            }}
        </Payment>
        {if (statutorySickPay.employersName.isDefined) {question(<Name/>, "employersName", statutorySickPay.employersName.get)}}
        {postalAddressStructureOpt("employersAddress", statutorySickPay.address, statutorySickPay.postCode.getOrElse("").toUpperCase)}
      </OtherMoneySSPDetails>
    }
    else NodeSeq.Empty
  }

  def otherMoneySPDetails(otherStatutoryPay: YesNoWithEmployerAndMoney) = {
    if (otherStatutoryPay.answer.toLowerCase == yes) {
      <OtherMoneySPDetails>
          <Payment>
           {questionCurrency(<Payment/>, "howMuch", otherStatutoryPay.howMuch)}
           {if(otherStatutoryPay.howOften.isDefined){
            {questionOther(<Frequency/>,"howOften_frequency", otherStatutoryPay.howOften.get.frequency, otherStatutoryPay.howOften.get.other)}
           }}
        </Payment>
        {if (otherStatutoryPay.employersName.isDefined) question(<Name/>, "employersName", otherStatutoryPay.employersName.getOrElse("empty"))}
        {postalAddressStructureOpt("employersAddress", otherStatutoryPay.address, otherStatutoryPay.postCode.getOrElse("").toUpperCase)}
      </OtherMoneySPDetails>
    }
    else NodeSeq.Empty
  }

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == yes
    case _ => false
  }
}