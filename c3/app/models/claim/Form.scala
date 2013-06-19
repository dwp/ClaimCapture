package models.claim

abstract class Form(val id: String)

trait BooleanConfirmation {
  val answer: Boolean
  def confirmation = if (answer) "Yes" else "No"
}