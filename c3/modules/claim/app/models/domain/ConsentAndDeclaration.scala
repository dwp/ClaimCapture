package models.domain

import controllers.Mappings._
import models.yesNo.{OptYesNoWithText, YesNoWithText}

object ConsentAndDeclaration extends Section.Identifier {
  val id = "s11"
}

case class AdditionalInfo(anythingElse: Option[String] = None, welshCommunication: String = "") extends QuestionGroup(AdditionalInfo)

object AdditionalInfo extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g1"
}

case class Consent(informationFromEmployer: OptYesNoWithText = OptYesNoWithText(answer = Some(""), text = None), informationFromPerson: YesNoWithText = YesNoWithText(answer="", text = None)) extends QuestionGroup(Consent)

object Consent extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g2"
}

case class Disclaimer(read: String = "") extends QuestionGroup(Disclaimer)

object Disclaimer extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g3"
}

case class Declaration(read: String = "", someoneElse: Option[String] = None) extends QuestionGroup(Declaration)

object Declaration extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g4"
}

object Submit extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g5"
}

object Error extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g6"
}