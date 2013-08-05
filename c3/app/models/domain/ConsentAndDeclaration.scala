package models.domain

import controllers.Mappings._
import play.api.mvc.Call

case class ConsentAndDeclaration(additionalInfo: AdditionalInfo, consent: Consent, disclaimer: Disclaimer, declaration: Declaration)

object ConsentAndDeclaration extends Section.Identifier {
  val id = "s11"
}

case class AdditionalInfo(call: Call, anythingElse: Option[String], welshCommunication:String) extends QuestionGroup(AdditionalInfo)

object AdditionalInfo extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g1"
}

case class Consent(call: Call,
                   informationFromEmployer: String, why: Option[String], informationFromPerson: String, whyPerson: Option[String]) extends QuestionGroup(Consent)

object Consent extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g2"

  def validateWhy(input: Consent): Boolean = input.informationFromEmployer match {
    case `no` => input.why.isDefined
    case `yes` => true
  }

  def validateWhyPerson(input: Consent): Boolean = input.informationFromPerson match {
    case `no` => input.whyPerson.isDefined
    case `yes` => true
  }
}

case class Disclaimer(call: Call, read: String) extends QuestionGroup(Disclaimer)

object Disclaimer extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g3"
}

case class Declaration(call: Call, read: String, someoneElse: Option[String]) extends QuestionGroup(Declaration)

object Declaration extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g4"
}

object Submit extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g5"
}

object Error extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g6"
}