package xml.claim

import app.XMLValues._
import controllers.mappings.Mappings
import models.domain._
import models.yesNo.{YesNoWith1MandatoryFieldOnYes, YesNoWith2MandatoryFieldsOnYes}
import xml.XMLComponent
import xml.XMLHelper._
import scala.xml.NodeSeq

object OtherBenefits extends XMLComponent {

  def xml(claim: Claim) = {
    val otherEEAState = claim.questionGroup[PaymentsFromAbroad].getOrElse(PaymentsFromAbroad())
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

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(YourPartnerPersonalDetails) match {
    case Some(p: YourPartnerPersonalDetails) => p.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim.update(createEEAStateFromXml(xml))
  }

  private def createEEAStateFromXml(xml: NodeSeq) = {
    val otherBenefits = (xml \\ "OtherBenefits" \ "EEA")
    models.domain.PaymentsFromAbroad(
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
