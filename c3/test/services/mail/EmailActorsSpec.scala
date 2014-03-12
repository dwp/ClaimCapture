package services.mail

import org.specs2.mutable.{Tags, Specification}
import akka.actor._
import specs2.akka.AkkaTestkitSpecs2Support
import com.typesafe.plugin.{MailerAPI, MailerPlugin}
import org.specs2.mock.Mockito


class EmailActorsSpec extends Specification with Tags with Mockito{

  "Email Manager" should {

    "start an email sending actor and receive email message" in new AkkaTestkitSpecs2Support {

      synchronized{SimpleSenderTestable.preStartCalls = 0}
      synchronized{SimpleSenderTestable.sendEmailsReceived = 0}

      val emailManager = system.actorOf(Props(classOf[EmailManagerTestable],Props[SimpleSenderTestable]))

      emailManager ! SendEmail()

      Thread.sleep(1000)

      SimpleSenderTestable.preStartCalls mustEqual 1
      SimpleSenderTestable.sendEmailsReceived mustEqual 1
    }
    "start an email sending actor" in new AkkaTestkitSpecs2Support() {
      import scala.collection.JavaConverters._

      val mailerMock = mock[MailerPlugin]
      mailerMock.email returns mock[MailerAPI]
      mailerMock.email setFrom any[String]                            returns mailerMock.email
      mailerMock.email setSubject any[String]                         returns mailerMock.email
      mailerMock.email setRecipient org.mockito.Matchers.anyVararg()  returns mailerMock.email

      val emailManager = system.actorOf(Props(classOf[EmailManagerTestable],Props(classOf[EmailSenderTestable],mailerMock)))

      val b = "Body content"
      val s = "my subject"
      val r = Seq("recipient1","recipient2")
      emailManager ! SendEmail(body = b,subject= s,to= r)

      Thread.sleep(1000)

      there was one(mailerMock.email).sendHtml(b)
      there was one(mailerMock.email).setSubject(s)
      there was one(mailerMock.email).setRecipient(r(0),r(1))

    }
  }

}

class SimpleSenderTestable extends Actor {
  override def receive: Actor.Receive = {
    case email:SendEmail => SimpleSenderTestable.sendEmailsReceived += 1
  }


  override def preStart() = {
    synchronized{ SimpleSenderTestable.preStartCalls += 1 }
  }
}

object SimpleSenderTestable {
  var preStartCalls = 0
  var sendEmailsReceived = 0
}

class EmailSenderTestable(mailPlugin:MailerPlugin) extends EmailSenderActor{
  override def mailerPluginApi: MailerAPI = mailPlugin.email
}

class EmailManagerTestable(emailSendingCreator:Props) extends EmailManagerActor(emailSendingCreator) {

}

object EmailManagerTestable {
  val preStartCalls = 0
  val exceptionRaised = 0

}
