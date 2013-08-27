package models.domain

import controllers.Mappings._
import models.yesNo.YesNoWithText

object ConsentAndDeclaration extends Section.Identifier {
  val id = "s11"
}

case class AdditionalInfo(anythingElse: Option[String] = None, welshCommunication: String = "") extends QuestionGroup(AdditionalInfo)

object AdditionalInfo extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g1"
}

case class Consent(informationFromEmployer: YesNoWithText = YesNoWithText(answer = "", text = None), informationFromPerson: YesNoWithText = YesNoWithText(answer="", text = None)) extends QuestionGroup(Consent)

object Consent extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g2"

  def validateWhy(input: Consent): Boolean = input.informationFromEmployer.answer match {
    case `no` => input.informationFromEmployer.text.isDefined
    case `yes` => true
  }

  def validateWhyPerson(input: Consent): Boolean = input.informationFromPerson.answer match {
    case `no` => input.informationFromPerson.text.isDefined
    case `yes` => true
  }
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