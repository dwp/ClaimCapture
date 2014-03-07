package services.mail

import scala.concurrent.duration._

import akka.actor.{OneForOneStrategy, Actor}
import akka.actor.SupervisorStrategy._

case class SendEmail(to: String, body: String,subject: String, from: String* )

class EmailManagerActor extends Actor {

  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 1 minute){
    case e:Exception => Restart
  }

  override def receive: Actor.Receive = {
    case e:SendEmail =>
  }
}


class EmailSenderActor extends Actor {

  override def receive: Actor.Receive = {
    case e:SendEmail => sendEmail(e)
  }


  def sendEmail(e:SendEmail) = mailerPluginApi.setFrom("ruben.diaz.valtech@gmail.com").setRecipient(e.from:_*).setSubject(e.subject).sendHtml(e.body)


  def mailerPluginApi = {
    import com.typesafe.plugin._
    use[MailerPlugin].email
  }
}
