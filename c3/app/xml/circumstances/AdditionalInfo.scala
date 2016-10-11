package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import models.domain.Claim

object AdditionalInfo {
  def xml(circs: Claim): NodeSeq = {
    <OtherChanges>{additionalInfo(circs)}</OtherChanges>
  }

  def additionalInfo(circs: Claim) = {
    val additionalInfoOption = circs.questionGroup[CircumstancesOtherInfo]
    lazy val selfEmploymentAdditionalInfoOption = circs.questionGroup[CircumstancesSelfEmployment]
    lazy val stoppedCaringOption = circs.questionGroup[CircumstancesStoppedCaring]
    lazy val paymentChangeOption = circs.questionGroup[CircumstancesPaymentChange]
    lazy val addressChangeOption = circs.questionGroup[CircumstancesAddressChange]
    lazy val employmentChangesOptions = circs.questionGroup[CircumstancesEmploymentChange]

    if (additionalInfoOption.isDefined) {
      additionalInfoOption.get.change
    } else if (selfEmploymentAdditionalInfoOption.isDefined) {
      selfEmploymentAdditionalInfoOption.get.moreAboutChanges.getOrElse("")
    } else if (stoppedCaringOption.isDefined) {
      stoppedCaringOption.get.moreAboutChanges.getOrElse("")
    } else if (paymentChangeOption.isDefined) {
      paymentChangeOption.get.moreAboutChanges.getOrElse("")
    } else if (addressChangeOption.isDefined) {
      addressChangeOption.get.moreAboutChanges.getOrElse("")
    } else if (employmentChangesOptions.isDefined) {
      if (employmentChangesOptions.get.typeOfWork.answer == "self-employed") {
        employmentChangesOptions.get.typeOfWork.text2b.getOrElse("")
      }
      else {
        circs.questionGroup[CircumstancesEmploymentPensionExpenses].get.moreAboutChanges.getOrElse("")
      }
    }
  }
}
