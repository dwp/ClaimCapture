package models.claim

import controllers.routes

object CarersAllowance {
  val id = "s1"
}

case class Benefits(answer: Boolean = false, action:String="next") extends Form with BooleanConfirmation with NavAction {
  val id = Benefits.id
  val url = routes.CarersAllowance.benefits()

  val next = routes.CarersAllowance.hours()
  val previous = url

}

object Benefits {
  val id = s"${CarersAllowance.id}.g1"
}

case class Hours(answer: Boolean = false, action:String="next") extends Form with BooleanConfirmation with NavAction {
  val id = Hours.id
  val url = routes.CarersAllowance.hours()
  val next = routes.CarersAllowance.livesInGB()
  val previous = routes.CarersAllowance.benefits()
}

object Hours {
  val id = s"${CarersAllowance.id}.g2"
}

case class LivesInGB(answer: Boolean = false, action:String="next") extends Form with BooleanConfirmation with NavAction {
  val id = LivesInGB.id
  val url = routes.CarersAllowance.livesInGB()
  val next = routes.CarersAllowance.over16()
  val previous = routes.CarersAllowance.hours()
}

object LivesInGB {
  val id = s"${CarersAllowance.id}.g3"
}

case class Over16(answer: Boolean = false, action:String="next") extends Form with BooleanConfirmation with NavAction {
  val id = Over16.id
  val url = routes.CarersAllowance.over16()
  val next = routes.CarersAllowance.approve()
  val previous = routes.CarersAllowance.livesInGB()
}

object Over16 {
  val id = s"${CarersAllowance.id}.g4"
}