package services.mail

import akka.actor.SupervisorStrategy._
import akka.actor.{OneForOneStrategy, _}
import org.postgresql.util.PSQLException
import play.api.Logger
import play.modules.mailer._
import services.ClaimTransactionComponent

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}
import app.ConfigProperties._
case class EmailWrapper(transactionId:String,email:Email)
object EmailActorsCreators {

  val emailSenderProps = Props[EmailSenderActor]

  lazy val emailManagerProps = Props(classOf[EmailManagerActor],emailSenderProps, getProperty("mail.retries",5), getProperty("mail.retriesTimeSpan",60))

  val SUCCESS = 0
  val ERROR = 1
}

class EmailManagerActor(emailSendingCreator:Props, retries:Int, retriesTimeSpan:Int) extends Actor {

  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = retries, withinTimeRange = retriesTimeSpan seconds){
    case e:PSQLException =>
      Logger.error("DB Error while updating email status.",e)
      Stop
    case e:Exception =>
      Logger.error("Could not send email.", e)
      Restart
  }

  override def receive: Actor.Receive = {
    case email:EmailWrapper => this.context.actorOf(emailSendingCreator) ! email
  }
}

class EmailSenderActor extends Actor with ClaimTransactionComponent{


  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {

    message match {
      case Some(m) => context.self ! m
      case _ => Logger.error("EmailSenderActor restarted without message after exception",reason)
    }
  }

  override def receive: Actor.Receive = {
    case e:EmailWrapper =>
      sendEmail(e)
      self ! PoisonPill
  }
  private def sendEmail(mail:EmailWrapper) = {

    mailerPluginApi.sendEmail(mail.email) match {
      case Success(s) =>
        claimTransaction.updateEmailStatus(mail.transactionId,EmailActorsCreators.SUCCESS)
        Logger.debug("Email sent successfully")
      case Failure(e) =>
        claimTransaction.updateEmailStatus(mail.transactionId,EmailActorsCreators.ERROR)
        throw e
    }
  }

  protected def mailerPluginApi = {
    Mailer
  }

  val claimTransaction = new ClaimTransaction
}
