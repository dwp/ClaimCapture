package models.claim

import controllers.routes

object CarersAllowance {
  val id = "s1"
}

case class Benefits(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Benefits.id
  val url = routes.CarersAllowance.benefits()
}

object Benefits {
  val id = s"${CarersAllowance.id}.g1"
}

case class Hours(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Hours.id
  val url = routes.CarersAllowance.hours()
}

object Hours {
  val id = s"${CarersAllowance.id}.g2"
}

case class LivesInGB(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = LivesInGB.id
  val url = routes.CarersAllowance.livesInGB()
}

object LivesInGB {
  val id = s"${CarersAllowance.id}.g3"
}

case class Over16(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Over16.id
  val url = routes.CarersAllowance.over16()
}

object Over16 {
  val id = s"${CarersAllowance.id}.g4"
}