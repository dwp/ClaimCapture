package models.claim

object CarersAllowance {
  val id = "s1"
}

case class Benefits(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Benefits.id
}

object Benefits {
  val id = s"${CarersAllowance.id}.g1"
}

case class Hours(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Hours.id
}

object Hours {
  val id = s"${CarersAllowance.id}.g2"
}

case class LivesInGB(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = LivesInGB.id
}

object LivesInGB {
  val id = s"${CarersAllowance.id}.g3"
}

case class Over16(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Over16.id
}

object Over16 {
  val id = s"${CarersAllowance.id}.g4"
}