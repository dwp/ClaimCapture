package models.claim

import play.api.mvc.Call

trait Form extends Confirmation with Approved {
  val id: String

  val url: Call
}

trait Confirmation {
  def confirmation: String = ""
}

trait Approved {
  def approved = false
}

trait BooleanConfirmation extends Confirmation with Approved {
  val answer: Boolean

  override def confirmation = if (answer) "Yes" else "No"

  override def approved = answer
}
