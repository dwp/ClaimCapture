package models.claim

import controllers.routes

case class BenefitsForm(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = BenefitsForm.id
  val url = routes.CarersAllowance.benefits()
}

object BenefitsForm {
  val id = "s1.g1"
}

case class HoursForm(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = HoursForm.id
  val url = routes.CarersAllowance.hours()
}

object HoursForm {
  val id = "s1.g2"
}

case class LivesInGBForm(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = LivesInGBForm.id
  val url = routes.CarersAllowance.livesInGB()
}

object LivesInGBForm {
  val id = "s1.g3"
}

case class Over16Form(val answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Over16Form.id
  val url = routes.CarersAllowance.over16()
}

object Over16Form {
  val id = "s1.g4"
}