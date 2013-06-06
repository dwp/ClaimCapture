package models

trait CarersAllowanceForm {
  val id: String

  def answerToString(): String

  lazy val section = id.split('.')(0)
}

abstract class BooleanAnswer(answer: Boolean) {
  def answerToString = if (answer) "Yes" else "No"
}

case class BenefitsForm(answer: Boolean = false) extends BooleanAnswer(answer) with CarersAllowanceForm {
  override val id = "s1.q1"
}

case class HoursForm(answer: Boolean = false) extends BooleanAnswer(answer) with CarersAllowanceForm {
  override val id = "s1.q2"
}

case class LivesInGBForm(answer: Boolean = false) extends BooleanAnswer(answer) with CarersAllowanceForm {
  override val id = "s1.q3"
}