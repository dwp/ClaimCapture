package xml

import models.domain.{Claim, CircumstancesOtherInfo}
import scala.xml.NodeSeq

object AdditionalInfo {
  def xml(circs: Claim): NodeSeq = {
    val additionalInfo = circs.questionGroup[CircumstancesOtherInfo].getOrElse(CircumstancesOtherInfo())

    <OtherChanges>{additionalInfo.change}</OtherChanges>
  }
}