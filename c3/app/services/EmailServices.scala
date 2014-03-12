package services

import play.api.templates.Html
import scala.language.existentials
import services.mail.{EmailActors, SendEmail}

object EmailServices {

  private[services] val SUBJECT = "Claim successfully finished"
  private[services] val BODY = "Your claim has finished without trouble"

  def sendEmail = new {
    private[services] var email = SendEmail()

    def to(r:String*) = EmailActors.manager ! email.copy(to=r.toSeq,subject = SUBJECT,body = BODY)

  }

}
