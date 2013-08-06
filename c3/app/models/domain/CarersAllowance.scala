package models.domain

import play.api.mvc.Call

case object CarersAllowance extends Section.Identifier {
  val id = "s1"
}

case class Benefits(call: Call, answer: Boolean = false) extends QuestionGroup(Benefits) with BooleanConfirmation

object Benefits extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g1"
}

case class BenefitsMandatory(call: Call, answerYesNo: String = "") extends QuestionGroup(BenefitsMandatory) with BooleanConfirmation
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object BenefitsMandatory extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g1"
}

case class Hours(call: Call, answer: Boolean = false) extends QuestionGroup(Hours) with BooleanConfirmation

object Hours extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g2"
}

case class HoursMandatory(call: Call, answerYesNo: String = "") extends QuestionGroup(HoursMandatory) with BooleanConfirmation
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object HoursMandatory extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g2"
}

case class Over16(call: Call, answer: Boolean = false) extends QuestionGroup(Over16) with BooleanConfirmation

object Over16 extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g3"
}

case class Over16Mandatory(call: Call, answerYesNo: String = "") extends QuestionGroup(HoursMandatory) with BooleanConfirmation
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object Over16Mandatory extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g2"
}

case class LivesInGB(call: Call, answer: Boolean = false) extends QuestionGroup(LivesInGB) with BooleanConfirmation

object LivesInGB extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g4"
}