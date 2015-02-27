package models.domain

case object CarersAllowance extends Section.Identifier {
  val id = "s1"
}

case class Benefits(benefitsAnswer: String = "") extends QuestionGroup(Benefits) with BooleanConfirmation
{
  val answer: Boolean = benefitsAnswer match {
    case Benefits.noneOfTheBenefits => false
    case _ => true
  }
}

object Benefits extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g1"

  val pip = "PIP"
  val dla = "DLA"
  val aa = "AA"
  val caa = "CAA"
  val afip = "AFIP"
  val noneOfTheBenefits = "NONE" // None of the benefits
}

case class Hours(answerYesNo: String = "") extends QuestionGroup(Hours) with BooleanConfirmation {
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object Hours extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g2"
}

case class Over16(answerYesNo: String = "") extends QuestionGroup(Over16) with BooleanConfirmation
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object Over16 extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g3"
}

case class LivesInGB(answerYesNo: String = "") extends QuestionGroup(LivesInGB) with BooleanConfirmation {
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object LivesInGB extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g4"
}

case class ProceedAnyway(allowedToContinue: Boolean, answerYesNo: Option[String] = None, jsEnabled: Boolean = false) extends QuestionGroup(ProceedAnyway) with BooleanConfirmation {
  val answer: Boolean = allowedToContinue || (answerYesNo match {
    case Some(answerYN) if answerYN == "yes" => true
    case _ => false
  })
}

object ProceedAnyway extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g6"
}