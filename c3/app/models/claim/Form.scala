package models.claim

import play.api.mvc.Call

trait Form extends Confirmation with NavAction {
  val id: String

  val url: Call

}

trait NavAction {
  val action:Option[String]

  val next: Call

  val previous:Call

  def findNext = {
    action match {
      case Some(action) => if(action == "next") { next } else previous
      case _ => next
    }
  }
}

object NavAction {
  val nextAction = Some("next")
}

trait Confirmation {
  def confirmation: String = ""
}

trait BooleanConfirmation extends Confirmation {
  val answer: Boolean

  override def confirmation = if (answer) "Yes" else "No"
}