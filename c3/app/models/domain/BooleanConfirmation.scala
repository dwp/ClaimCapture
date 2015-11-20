package models.domain

trait BooleanConfirmation {
  val answer: Boolean

  def confirmation = if (answer) "Yes" else "No"
}
