package models.view.example

case class SingleStringInputForm(answer: String = "") extends CarersAllowanceForm {
  override val id = "remove generic"

  override def answerToString(): String = answer

  override def toString: String = answer
}