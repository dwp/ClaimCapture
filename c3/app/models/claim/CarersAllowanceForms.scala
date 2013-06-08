package models.claim

case class BenefitsForm(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = "s1.g1"
}

case class HoursForm(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = "s1.g2"
}

case class LivesInGBForm(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = "s1.g3"
}

case class Over16Form(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = "s1.g4"
}