package services.mail

import scala.concurrent.duration._
import scala.language.postfixOps
import akka.actor.{Props, ActorSystem, OneForOneStrategy, Actor}
import akka.actor.SupervisorStrategy._
import play.api.Play.current


class EmailManagerActor extends Actor {

  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 1 minute){
    case e:Exception => printf(e.getStackTraceString);Restart
  }

  override def receive: Actor.Receive = {
    case e:SendEmail =>
      val actor = this.context.actorOf(Props[EmailSenderActor])
      actor ! e
      actor ! Stop

  }
}


class EmailSenderActor extends Actor {

  override def receive: Actor.Receive = {
    case e:SendEmail => sendEmail(e)
  }
  def sendEmail(mail:SendEmail) = mailerPluginApi.setFrom("ruben.diaz.valtech@gmail.com").setRecipient(mail.to:_*).setSubject(mail.subject).sendHtml(mail.body)

  def mailerPluginApi = {
    import com.typesafe.plugin._
    use[MailerPlugin].email
  }
}
