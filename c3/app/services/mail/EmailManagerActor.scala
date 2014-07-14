package services.mail

import scala.concurrent.duration._
import scala.language.postfixOps
import akka.actor._
import akka.actor.SupervisorStrategy._
import play.api.Play.current
import play.api.Logger
import akka.actor.OneForOneStrategy

object EmailActorsCreators {

  val emailSenderProps = Props[EmailSenderActor]

  val emailManagerProps = Props(classOf[EmailManagerActor],emailSenderProps)
}

class EmailManagerActor(emailSendingCreator:Props) extends Actor {

  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 1 minute){
    case e:Exception =>
      Logger.error("Could not send email.", e)
      Restart
  }

  override def receive: Actor.Receive = {
    case email:SendEmail => this.context.actorOf(emailSendingCreator) ! email
  }
}

class EmailSenderActor extends Actor {


  override def receive: Actor.Receive = {
    case e:SendEmail =>
      sendEmail(e)
      self ! PoisonPill
  }
  def sendEmail(mail:SendEmail) = {
    import app.ConfigProperties._
    mailerPluginApi.setFrom(getProperty("mailer.from","noreply@carersallowance.service.gov.uk"))
      .setRecipient(mail.to:_*)
      .setSubject(mail.subject)
      .sendHtml(mail.body)
  }

  def mailerPluginApi = {
    import com.typesafe.plugin._
    use[MailerPlugin].email
  }
}
