package models.claim

abstract class Form(val id: String) extends Confirmation

trait Confirmation {
  def confirmation: String = ""
}

trait BooleanConfirmation extends Confirmation {
  val answer: Boolean

  override def confirmation = if (answer) "Yes" else "No"
}