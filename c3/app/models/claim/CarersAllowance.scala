package models.claim

import controllers.routes

object CarersAllowance {
  val id = "s1"
}

case class Benefits(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Benefits.id

  val next = routes.CarersAllowance.hours()
  val previous = routes.CarersAllowance.benefits()
}

object Benefits {
  val id = s"${CarersAllowance.id}.g1"
}

case class Hours(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Hours.id

  val next = routes.CarersAllowance.livesInGB()
  val previous = routes.CarersAllowance.benefits()
}

object Hours {
  val id = s"${CarersAllowance.id}.g2"
}

case class LivesInGB(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = LivesInGB.id

  val next = routes.CarersAllowance.over16()
  val previous = routes.CarersAllowance.hours()
}

object LivesInGB {
  val id = s"${CarersAllowance.id}.g3"
}

case class Over16(answer: Boolean = false) extends Form with BooleanConfirmation {
  val id = Over16.id

  val next = routes.CarersAllowance.approve()
  val previous = routes.CarersAllowance.livesInGB()
}

object Over16 {
  val id = s"${CarersAllowance.id}.g4"
}