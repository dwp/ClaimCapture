package models

abstract class CarersAllowanceForm(val id: String, val answer: Boolean = false) {
  val section = id.split('.')(0)

  def answerToString = if (answer) "Yes" else "No"
}

case class BenefitsForm(override val answer: Boolean = false) extends CarersAllowanceForm("s1.g1", answer)

case class HoursForm(override val answer: Boolean = false) extends CarersAllowanceForm("s1.g2", answer)

case class LivesInGBForm(override val answer: Boolean = false) extends CarersAllowanceForm("s1.g3", answer)

case class Over16Form(override val answer: Boolean = false) extends CarersAllowanceForm("s1.g4", answer)