package models.domain

object CarersAllowance {
  val id = "s1"
}

case class Benefits(answer: Boolean = false) extends QuestionGroup(Benefits.id) with BooleanConfirmation

object Benefits {
  val id = s"${CarersAllowance.id}.g1"
}

case class Hours(answer: Boolean = false) extends QuestionGroup(Hours.id) with BooleanConfirmation

object Hours {
  val id = s"${CarersAllowance.id}.g2"
}

case class Over16(answer: Boolean = false) extends QuestionGroup(Over16.id) with BooleanConfirmation

object Over16 {
  val id = s"${CarersAllowance.id}.g3"
}

case class LivesInGB(answer: Boolean = false) extends QuestionGroup(LivesInGB.id) with BooleanConfirmation

object LivesInGB {
  val id = s"${CarersAllowance.id}.g4"
}