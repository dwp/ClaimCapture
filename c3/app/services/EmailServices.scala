package services

import play.api.templates.Html
import scala.language.existentials
import services.mail.{EmailActors, SendEmail}

object EmailServices {

  def sendEmail = new {
    private[services] var email = SendEmail()
    def to(r:String*) =  {
      email = email.copy(to=r.toSeq)
      this
    }
    def withSubject(subject:String) = {
      email = email.copy(subject = subject)
      this
    }
    def withBody(html:Html):Unit = EmailActors.manager ! email.copy(body = html.body)

    def withBody(message:String):Unit = EmailActors.manager ! email.copy(body = message)

  }

}
