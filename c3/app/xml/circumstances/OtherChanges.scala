package xml.circumstances

import models.domain.{CircumstancesSelfEmployment, Claim, CircumstancesOtherInfo}
import scala.xml.NodeSeq
import xml.XMLComponent
import xml.XMLHelper._

object OtherChanges extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
    question(<OtherChanges/>,"c2.g1",additionalInfo(circs))
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