package models.domain

import models.yesNo.YesNoWithText


object Information extends Section.Identifier {
  val id = "s10"
}

case class AdditionalInfo(anythingElse: YesNoWithText = YesNoWithText(answer = "", text = None), welshCommunication: String = "") extends QuestionGroup(AdditionalInfo)

object AdditionalInfo extends QuestionGroup.Identifier {
  val id = s"${Information.id}.g1"
}
