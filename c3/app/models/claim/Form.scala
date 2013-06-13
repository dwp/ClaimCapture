package models.claim

trait Form extends Confirmation {
  val id: String
}

trait Confirmation {
  def confirmation: String = ""
}

trait BooleanConfirmation extends Confirmation {
  val answer: Boolean

  override def confirmation = if (answer) "Yes" else "No"
}