package models.domain

case object CarersAllowance {
  val id = "s1"
}

case class Benefits(answer: Boolean = false) extends QuestionGroup(Benefits.id) with BooleanConfirmation

case object Benefits extends QuestionGroup(s"${CarersAllowance.id}.g1")

case class Hours(answer: Boolean = false) extends QuestionGroup(Hours.id) with BooleanConfirmation

case object Hours extends QuestionGroup(s"${CarersAllowance.id}.g2")

case class Over16(answer: Boolean = false) extends QuestionGroup(Over16.id) with BooleanConfirmation

case object Over16 extends QuestionGroup(s"${CarersAllowance.id}.g3")

case class LivesInGB(answer: Boolean = false) extends QuestionGroup(LivesInGB.id) with BooleanConfirmation

case object LivesInGB extends QuestionGroup(s"${CarersAllowance.id}.g4")