package xml

import models.domain.{CircumstancesOtherInfo, Circs}
import scala.xml.NodeSeq

object AdditionalInfo {

  def xml(circs: Circs):NodeSeq = {

    val additionalInfo = circs.questionGroup[CircumstancesOtherInfo].getOrElse(CircumstancesOtherInfo())

    <OtherChanges>{additionalInfo.change}</OtherChanges>
  }

}
