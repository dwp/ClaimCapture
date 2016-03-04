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

  val textMaxLength:Integer={
    val schemaVersion=getProperty("xml.schema.version", "xml.schema.version not found")
    lazy val validation=new SchemaValidation(schemaVersion)
    Option(validation.getRestriction("OtherInformation//AdditionalInformation//Why//Answer")) match {
      case Some(restriction) => if( restriction.getMaxlength!=null) restriction.getMaxlength else 2990
      case _ => 2990
    }
  }
}
