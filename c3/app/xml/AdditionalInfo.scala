package xml

import models.domain.{CircumstancesSelfEmployment, Claim, CircumstancesOtherInfo}
import scala.xml.NodeSeq

object AdditionalInfo {
  def xml(circs: Claim): NodeSeq = {
    <OtherChanges>{additionalInfo(circs)}</OtherChanges>
  }

  def additionalInfo(circs: Claim) = {
    val additionalInfoOption = circs.questionGroup[CircumstancesOtherInfo]
    lazy val selfEmploymentAdditionalInfoOption = circs.questionGroup[CircumstancesSelfEmployment]

    if (additionalInfoOption.isDefined) {
      additionalInfoOption.get.change
    } else if (selfEmploymentAdditionalInfoOption.isDefined) {
      selfEmploymentAdditionalInfoOption.get.moreAboutChanges.getOrElse("")
    }
  }
}