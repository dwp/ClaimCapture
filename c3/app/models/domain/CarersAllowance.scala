package models.domain

case object CarersAllowance extends Section.Identifier {
  val id = "s1"
}

case class BenefitsMandatory(answerYesNo: String = "") extends QuestionGroup(BenefitsMandatory) with BooleanConfirmation with NoRouting
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object BenefitsMandatory extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g1"
}

case class HoursMandatory(answerYesNo: String = "") extends QuestionGroup(HoursMandatory) with BooleanConfirmation with NoRouting
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object HoursMandatory extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g2"
}

case class Over16Mandatory(answerYesNo: String = "") extends QuestionGroup(Over16Mandatory) with BooleanConfirmation with NoRouting
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object Over16Mandatory extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g3"
}

case class LivesInGBMandatory(answerYesNo: String = "") extends QuestionGroup(LivesInGBMandatory) with BooleanConfirmation with NoRouting
{
  val answer: Boolean = answerYesNo match {
    case "yes" => true
    case _ => false
  }
}

object LivesInGBMandatory extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g4"
}