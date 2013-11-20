package xml.circumstances

import models.domain.{Claim, CircumstancesOtherInfo}
import scala.xml.NodeSeq
import xml.XMLComponent

object AdditionalInfo extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
    val additionalInfo = circs.questionGroup[CircumstancesOtherInfo].getOrElse(CircumstancesOtherInfo())

    <OtherChanges>{additionalInfo.change}</OtherChanges>
  }
}