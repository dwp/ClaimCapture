package services.mail

import akka.actor._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.Logger
import play.modules.mailer._
import specs2.akka.AkkaTestkitSpecs2Support

import scala.util.{Failure, Success}


class EmailActorsSpec extends Specification with Mockito{

  sequential

  "Email Manager" should {

    "start an email sending actor and receive email message" in new AkkaTestkitSpecs2Support {

      synchronized{SimpleSenderTestable.preStartCalls = 0}
      synchronized{SimpleSenderTestable.sendEmailsReceived = 0}

      val emailManager = system.actorOf(Props(classOf[EmailManagerTestable],Props[SimpleSenderTestable]))

      emailManager ! mock[EmailWrapper]

      Thread.sleep(1000)

      SimpleSenderTestable.preStartCalls mustEqual 1
      SimpleSenderTestable.sendEmailsReceived mustEqual 1
    }

    "start an email sending actor" in new AkkaTestkitSpecs2Support() {

      val mailerMock = successMailerMock

      val emailManager = system.actorOf(Props(classOf[EmailManagerTestable],Props(classOf[EmailSenderTestable],mailerMock)))

      val b = "Body content"
      val s = "my subject"
      val r = Seq("recipient1","recipient2")
      val email = Email(s,EmailAddress(null,""),"",b,recipients = r.map(r => Recipient(RecipientType.TO,EmailAddress(null,r))),replyTo = None)
      emailManager ! EmailWrapper("",email)

      Thread.sleep(1000)

      there was one(mailerMock).sendEmail(email)

    }

    "successfully manage 5 email sending actors" in new AkkaTestkitSpecs2Support {

      val mailerMock = successMailerMock

      val emailManager = system.actorOf(Props(classOf[EmailManagerTestable],Props(classOf[WaitingEmailSenderTestable],mailerMock)))

      val b = "Body content"
      val s = "my subject"
      val r = Seq("recipient1","recipient2")
      val email = Email(s,EmailAddress(null,""),"",b,recipients = r.map(r => Recipient(RecipientType.TO,EmailAddress(null,r))),replyTo = None)
      for(i <- 1 to 5) {
        emailManager ! EmailWrapper("", email)
      }

      Thread.sleep(5000)

      there was atLeast(5)(mailerMock).sendEmail(email)
    }

    "restart the email actor because of failures" in new AkkaTestkitSpecs2Support() {
      synchronized{EmailSenderTestable.sendEmail = 0}

      val mail = mock[EmailWrapper]
      mail.transactionId returns "TEST1234"
      mail.email returns mock[Email]
      val mailerMock = mock[Mailer]

      mailerMock.sendEmail(any[Email]).returns(Failure(new Exception("Test exception")))

      val emailManager = system.actorOf(Props(classOf[EmailManagerTestable],Props(classOf[EmailSenderTestable],mailerMock)))

      emailManager ! mail

      Thread.sleep(5000)


      EmailSenderTestable.synchronized{
        EmailSenderTestable.sendEmail must beLessThanOrEqualTo(1)
      }

      there was atMost(3)(mailerMock).sendEmail(any[Email])

    }
  }
  section ("unit","slow")

  def successMailerMock: Mailer = {
    val mailerMock = mock[Mailer]
    mailerMock.sendEmail(any[Email]) returns Success(Unit)
    mailerMock
  }
}

class SimpleSenderTestable extends Actor {
  override def receive: Actor.Receive = {
    case email:EmailWrapper => SimpleSenderTestable.sendEmailsReceived += 1
  }


  override def preStart() = {
    synchronized{ SimpleSenderTestable.preStartCalls += 1 }
  }
}

object SimpleSenderTestable {
  var preStartCalls = 0
  var sendEmailsReceived = 0
}

class EmailSenderTestable(mailPlugin:Mailer) extends EmailSenderActor(5){

  override def mailerPluginApi: Mailer = mailPlugin


  override protected def sendEmail(mail: EmailWrapper): Unit = {

    EmailSenderTestable.synchronized{
      EmailSenderTestable.sendEmail += 1
      Logger.info("sendEmail called "+EmailSenderTestable.sendEmail+" times")
    }
    super.sendEmail(mail)
  }

  override def postStop(): Unit = {
    Logger.info("Stopped actor")
  }

  override val claimTransaction = new StubClaimTransaction

}

class WaitingEmailSenderTestable(mailPlugin:Mailer) extends EmailSenderActor(5){

  override def mailerPluginApi: Mailer = mailPlugin

  override protected def sendEmail(mail: EmailWrapper): Unit = {
    Thread.sleep(1000)
    super.sendEmail(mail)
  }

  override def postStop(): Unit = {
    Logger.info("Stopped actor")
  }

  override val claimTransaction = new StubClaimTransaction

}

object EmailSenderTestable{
  var sendEmail = 0
}

class EmailManagerTestable(emailSendingCreator:Props) extends EmailManagerActor(emailSendingCreator) {


}


object EmailManagerTestable {
  val preStartCalls = 0
  val exceptionRaised = 0

}
