package models.domain

import controllers.Mappings._

case class ConsentAndDeclaration(additionalInfo: AdditionalInfo,consent: Consent,disclaimer: Disclaimer,declaration: Declaration)
object ConsentAndDeclaration {
  val id = "s7"
}


object AdditionalInfo extends QuestionGroup(s"${ConsentAndDeclaration.id}.g1")
case class AdditionalInfo(anythingElse:Option[String],welshCommunication:String) extends QuestionGroup(AdditionalInfo.id)

object Consent extends QuestionGroup(s"${ConsentAndDeclaration.id}.g2"){

  def validateWhy(input: Consent): Boolean = input.informationFromEmployer match {
    case `no` => input.why.isDefined
    case `yes` => true
  }

  def validateWhyPerson(input: Consent): Boolean = input.informationFromPerson match {
    case `no` => input.whyPerson.isDefined
    case `yes` => true
  }

}
case class Consent(informationFromEmployer:String, why:Option[String],informationFromPerson:String,whyPerson:Option[String]) extends QuestionGroup(Consent.id)



object Disclaimer extends QuestionGroup(s"${ConsentAndDeclaration.id}.g3")
case class Disclaimer(read:String) extends QuestionGroup(Disclaimer.id)

object Declaration extends QuestionGroup(s"${ConsentAndDeclaration.id}.g4")
case class Declaration(confirm:String,someoneElse: String) extends QuestionGroup(Declaration.id)

object Submit extends QuestionGroup(s"${ConsentAndDeclaration.id}.g5")
object Error extends QuestionGroup(s"${ConsentAndDeclaration.id}.g6")


