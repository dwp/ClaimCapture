package xml.circumstances

import models.domain.{Claim, CircumstancesOtherInfo}
import scala.xml.NodeSeq
import xml.XMLComponent
import xml.XMLHelper._

object OtherChanges extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
    val additionalInfo = circs.questionGroup[CircumstancesOtherInfo].getOrElse(CircumstancesOtherInfo())

    question(<OtherChanges/>,"c2.g1",additionalInfo.change)
  }
}