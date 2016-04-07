package xml.claim

import app.XMLValues._
import controllers.mappings.Mappings
import models.domain._
import models.yesNo.{YesNo, YesNoWith1MandatoryFieldOnYes, YesNoWith2MandatoryFieldsOnYes, YesNoWithEmployerAndMoney}
import xml.XMLComponent
import xml.XMLHelper._
import scala.xml.NodeSeq

object OtherBenefits extends XMLComponent {

  def xml(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
//    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
//    val statutorySickPay = aboutOtherMoney.statutorySickPay
//    val otherStatutoryPayOption = aboutOtherMoney.otherStatutoryPay
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())


//      {question(<OtherMoneySSP/>,"haveYouHadAnyStatutorySickPay.label", statutorySickPay.answer)}
//      {otherMoneySPPXml(statutorySickPay)}
//      {question(<OtherMoneySP/>,"otherPay.label",otherStatutoryPayOption.answer)}
//      {otherMoneySPDetails(otherStatutoryPayOption)}
//      {question(<OtherMoneyPayments/>,"anyPaymentsSinceClaimDate.answer",aboutOtherMoney.anyPaymentsSinceClaimDate.answer)}
//      {aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
//          case "yes" =>{
//            <OtherMoneyDetails>
//              <Payment>
//                {questionCurrency(<Payment/>,"howMuch.label", aboutOtherMoney.howMuch)}
//                {if(aboutOtherMoney.howOften.isDefined){
//                  {questionOther(<Frequency/>,"howOften", aboutOtherMoney.howOften.get.frequency, aboutOtherMoney.howOften.get.other)}
//                }}
//              </Payment>
//              {question(<Name/>,"whoPaysYou.label", aboutOtherMoney.whoPaysYou)}
//              </OtherMoneyDetails>
//          }
//          case "no" => NodeSeq.Empty
//          case _ => throw new RuntimeException("AnyPaymentsSinceClaimDate is either Yes Or No")
//        }}
    <OtherBenefits>
      <EEA>
        {question(<EEAGuardQuestion/>,"eeaGuardQuestion.answer", otherEEAState.guardQuestion.answer)}
        {question(<EEAReceivePensionsBenefits/>,"benefitsFromEEA", otherEEAState.guardQuestion.field1.fold("")(_.answer))}
        {question(<EEAReceivePensionsBenefitsDetails/>,"benefitsFromEEADetails", otherEEAState.guardQuestion.field1.fold(Option[String](""))(_.field))}
        {question(<EEAWorkingInsurance/>,"workingForEEA", otherEEAState.guardQuestion.field2.fold("")(_.answer))}
        {question(<EEAWorkingInsuranceDetails/>,"workingForEEADetails", otherEEAState.guardQuestion.field2.fold(Option[String](""))(_.field))}
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

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(YourPartnerPersonalDetails) match {
    case Some(p: YourPartnerPersonalDetails) => p.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim
      //.update(createOtherMoneyDetailsFromXml(xml))
      .update(createEEAStateFromXml(xml))
  }

//  private def createOtherMoneyDetailsFromXml(xml: NodeSeq) = {
//    val otherBenefits = (xml \\ "OtherBenefits")
//    models.domain.AboutOtherMoney(
//      anyPaymentsSinceClaimDate = YesNo(answer = createYesNoText((otherBenefits \ "OtherMoneyPayments" \ "Answer").text)),
//      whoPaysYou = createStringOptional((otherBenefits \ "OtherMoneyDetails" \ "Name" \ "Answer").text),
//      howMuch = createStringOptional((otherBenefits \ "OtherMoneyDetails" \ "Payment" \ "Payment" \ "Answer" \ "Amount").text),
//      howOften = createPaymentFrequencyOptionalFromXml((otherBenefits \ "OtherMoneyDetails" \ "Payment"), "Frequency"),
//      statutorySickPay = createOtherMoneySSPDetailsFromXml(otherBenefits),
//      otherStatutoryPay = createOtherMoneySPDetailsFromXml(otherBenefits)
//    )
//  }

  private def createOtherMoneySPDetailsFromXml(otherBenefits: NodeSeq) = {
    val otherMoneySPDetails = (otherBenefits \\ "OtherMoneySPDetails")
    otherMoneySPDetails.isEmpty match {
      case false =>
        models.yesNo.YesNoWithEmployerAndMoney(
          answer = Mappings.yes,
          howMuch = createStringOptional((otherMoneySPDetails \ "Payment" \ "Payment" \ "Answer" \ "Amount").text),
          howOften = createPaymentFrequencyOptionalFromXml((otherMoneySPDetails \ "Payment"), "Frequency"),
          employersName = createStringOptional((otherMoneySPDetails \ "Name" \ "Answer").text),
          address = createAddressOptionalFromXml(otherMoneySPDetails),
          postCode = createStringOptional((otherMoneySPDetails \ "Address" \ "Answer" \ "PostCode").text)
        )
      case true => models.yesNo.YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None)
    }
  }

  private def createOtherMoneySSPDetailsFromXml(otherBenefits: NodeSeq) = {
    val otherMoneySSPDetails = (otherBenefits \\ "OtherMoneySSPDetails")
    otherMoneySSPDetails.isEmpty match {
      case false =>
        models.yesNo.YesNoWithEmployerAndMoney(
          answer = Mappings.yes,
          howMuch = createStringOptional((otherMoneySSPDetails \ "Payment" \ "Payment" \"Answer" \ "Amount").text),
          howOften = createPaymentFrequencyOptionalFromXml((otherMoneySSPDetails \ "Payment"), "Frequency"),
          employersName = createStringOptional((otherMoneySSPDetails \ "Name" \ "Answer").text),
          address = createAddressOptionalFromXml((otherMoneySSPDetails)),
          postCode = createStringOptional((otherMoneySSPDetails \ "Address" \ "Answer" \ "PostCode").text)
        )
      case true => models.yesNo.YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None)
    }
  }

  private def createEEAStateFromXml(xml: NodeSeq) = {
    val otherBenefits = (xml \\ "OtherBenefits" \ "EEA")
    models.domain.OtherEEAStateOrSwitzerland(
      guardQuestion = YesNoWith2MandatoryFieldsOnYes(
        answer = createYesNoText((otherBenefits \ "EEAGuardQuestion" \ "Answer").text),
        field1 = createYesNoWith1MandatoryFieldOnYesOptional(otherBenefits, (otherBenefits \ "EEAGuardQuestion" \ "Answer").text, "EEAReceivePensionsBenefits", "EEAReceivePensionsBenefitsDetails"),
        field2 = createYesNoWith1MandatoryFieldOnYesOptional(otherBenefits, (otherBenefits \ "EEAGuardQuestion" \ "Answer").text, "EEAWorkingInsurance", "EEAWorkingInsuranceDetails")
      )
    )
  }

  private def createYesNoWith1MandatoryFieldOnYesOptional(otherBenefits: NodeSeq, guardQuestion: String, answerElement: String, textElement: String) = {
    guardQuestion.toLowerCase() match {
      case Mappings.yes =>
        Some(YesNoWith1MandatoryFieldOnYes(
          answer = createYesNoText((otherBenefits \ answerElement \ "Answer").text),
          field = createStringOptional((otherBenefits \ textElement \ "Answer").text)))
      case _ => None
    }
  }
}
