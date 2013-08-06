package models.domain

case object CarersAllowance extends Section.Identifier {
  val id = "s1"
}

case class Benefits(answer: Boolean = false) extends QuestionGroup(Benefits) with BooleanConfirmation with NoRouting

object Benefits extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g1"
}

case class Hours(answer: Boolean = false) extends QuestionGroup(Hours) with BooleanConfirmation with NoRouting

object Hours extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g2"
}

case class Over16(answer: Boolean = false) extends QuestionGroup(Over16) with BooleanConfirmation with NoRouting

object Over16 extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g3"
}

case class LivesInGB(answer: Boolean = false) extends QuestionGroup(LivesInGB) with BooleanConfirmation with NoRouting

object LivesInGB extends QuestionGroup.Identifier {
  val id = s"${CarersAllowance.id}.g4"
}