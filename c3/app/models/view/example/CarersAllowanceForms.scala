package models.view.example

trait CarersAllowanceForm {
  val id: String
  def answerToString(): String
}

case class BenefitsForm(answer: Boolean = false) extends CarersAllowanceForm {
  override val id = "s1.q1"

  override def answerToString = if (answer) "Yes" else "No"
}