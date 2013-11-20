package xml.circumstances

import models.domain.{Claim, CircumstancesOtherInfo}
import scala.xml.NodeSeq
import xml.XMLComponent
import play.api.i18n.Messages

object OtherChanges extends XMLComponent {
  def xml(circs: Claim): NodeSeq = {
    val additionalInfo = circs.questionGroup[CircumstancesOtherInfo].getOrElse(CircumstancesOtherInfo())

    <OtherChanges>
      <QuestionLabel>{Messages("c2.g1")}</QuestionLabel>
      <Answer>{additionalInfo.change}</Answer>
    </OtherChanges>
  }
}