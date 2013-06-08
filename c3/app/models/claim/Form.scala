package models.claim

trait Form extends Confirmation with Approved {
  val id: String

  def section = id.split('.')(0)
}

trait Confirmation {
  def confirmation: String
}

trait Approved {
  def approved: Boolean
}

trait BooleanConfirmation extends Confirmation with Approved {
  val answer: Boolean

  override def confirmation = if (answer) "Yes" else "No"

  override def approved = answer
}