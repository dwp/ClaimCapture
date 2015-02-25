package models.domain

import controllers.mappings.Mappings
import app.ConfigProperties._

/**
 * Created by valtechuk on 24/02/2015.
 */
trait EMail {
  val wantsContactEmail:Option[String] = None
  val email:Option[String] = None
  val emailConfirmation:Option[String] = None
}

object EMail{
  def emailConfirmation[T <: EMail](emailData:T) = {
    emailData.email == emailData.emailConfirmation
  }

  def emailRequired[T <: EMail](emailData:T) = {
    if (emailData.wantsContactEmail.getOrElse(Mappings.no) == Mappings.yes) emailData.email.nonEmpty
    else true
  }

  def wantsEmailRequired[T <: EMail](emailData:T) = {
    if(getProperty("email.frontend",false) && emailData.wantsContactEmail.isEmpty) false
    else true
  }
}
