package models.domain

object ConsentAndDeclaration {
  val id = "s7"
}


object Consent extends QuestionGroup(s"${ConsentAndDeclaration.id}.g1")
case class Consent(informationFromEmployer:String, why:String,informationFromPerson:String) extends QuestionGroup(Consent.id)

object Disclaimer extends QuestionGroup(s"${ConsentAndDeclaration.id}.g2")
case class Disclaimer(read:String) extends QuestionGroup(Disclaimer.id)

object Declaration extends QuestionGroup(s"${ConsentAndDeclaration.id}.g3")
case class Declaration(read:String) extends QuestionGroup(Declaration.id)

object AdditionalInfo extends QuestionGroup(s"${ConsentAndDeclaration.id}.g4")
case class AdditionalInfo(anythingElse:Option[String],welshCommunication:String) extends QuestionGroup(AdditionalInfo.id)
