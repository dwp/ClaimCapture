package services.mail

import akka.actor.SupervisorStrategy._
import akka.actor.{OneForOneStrategy, _}
import play.api.Logger
import play.modules.mailer._

import scala.concurrent.duration._
import scala.language.postfixOps

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
    case email:Email => this.context.actorOf(emailSendingCreator) ! email
  }
}

class EmailSenderActor extends Actor {


  override def receive: Actor.Receive = {
    case e:Email =>
      sendEmail(e)
      self ! PoisonPill
  }
  def sendEmail(mail:Email) = {

    Logger.debug(s"EmailManagerActor sending email $mail")
    mailerPluginApi.sendEmail(mail)

      /*.setFrom(getProperty("mailer.from","noreply@carersallowance.service.gov.uk"))
      .setRecipient(mail.to:_*)
      .setSubject(mail.subject)
      .sendHtml(mail.body)*/
  }

  def mailerPluginApi = {
    Mailer
  }
}
