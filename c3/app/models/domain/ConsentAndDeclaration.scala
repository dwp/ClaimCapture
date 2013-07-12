package models.domain

case class ConsentAndDeclaration(additionalInfo: AdditionalInfo,consent: Consent)
object ConsentAndDeclaration {
  val id = "s7"
}


object AdditionalInfo extends QuestionGroup(s"${ConsentAndDeclaration.id}.g1")
case class AdditionalInfo(anythingElse:Option[String],welshCommunication:String) extends QuestionGroup(AdditionalInfo.id)

object Consent extends QuestionGroup(s"${ConsentAndDeclaration.id}.g2")
case class Consent(informationFromEmployer:String, why:String,informationFromPerson:String) extends QuestionGroup(Consent.id)

object Disclaimer extends QuestionGroup(s"${ConsentAndDeclaration.id}.g3")
case class Disclaimer(read:String) extends QuestionGroup(Disclaimer.id)

object Declaration extends QuestionGroup(s"${ConsentAndDeclaration.id}.g4")
case class Declaration(read:String) extends QuestionGroup(Declaration.id)

object Submit extends QuestionGroup(s"${ConsentAndDeclaration.id}.g5")
object Error extends QuestionGroup(s"${ConsentAndDeclaration.id}.g6")


