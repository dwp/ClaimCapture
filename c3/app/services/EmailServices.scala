package services

import play.api.templates.Html
import play.api.Play.current
import scala.language.existentials

object EmailServices {

  import com.typesafe.plugin._

  def sendEmail = new {
    private[services] val api = use[MailerPlugin].email.setFrom("ruben.diaz.valtech@gmail.com")
    def to(recipients:String*) =  {
      api.setRecipient(recipients:_*)
      this
    }
    def withSubject(subject:String) = {
      api.setSubject(subject)
      this
    }
    def withBody(html:Html) = api.sendHtml(html.body)

    def withBody(message:String) = api.send(message)


  }

}
