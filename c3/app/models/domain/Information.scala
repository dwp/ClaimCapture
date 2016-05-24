package models.domain

import gov.dwp.carers.xml.schemavalidations.SchemaValidation
import models.yesNo.YesNoWithText
import app.ConfigProperties._

object Information extends Section.Identifier {
  val id = "s11"
}

case class AdditionalInfo(anythingElse: YesNoWithText = YesNoWithText(answer = "", text = None), welshCommunication: String = "") extends QuestionGroup(AdditionalInfo)

object AdditionalInfo extends QuestionGroup.Identifier {
  val id = s"${Information.id}.g1"

  def textMaxLength:Integer={
    val schemaVersion=getStringProperty("xml.schema.version")
    Option(new SchemaValidation(schemaVersion).getRestriction("OtherInformation//AdditionalInformation//Why//Answer")) match {
      case Some(restriction) => if( restriction.getMaxlength!=null) restriction.getMaxlength else -1
      case _ => -1
    }
  }
}
