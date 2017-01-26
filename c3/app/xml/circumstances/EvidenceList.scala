package xml.circumstances

import controllers.mappings.Mappings
import models.domain._
import app.XMLValues._
import models.domain.Claim
import scala.xml.NodeSeq
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

object EvidenceList {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def buildXml(circs: Claim) = {
    <EvidenceList>
      {evidence(circs)}
    </EvidenceList>
  }

  def evidence(circs: Claim): NodeSeq = {
    import xml.claim.EvidenceList._

    val isBreaksInCare = showBreaksInCaremessages(circs)
    val isEmployment1 = circs.questionGroup[CircumstancesStartedEmploymentAndOngoing].isDefined
    val isEmployment2 = circs.questionGroup[CircumstancesStartedAndFinishedEmployment].isDefined
    val isEmail = circs.questionGroup[ContactDetails].getOrElse(ContactDetails()).wantsContactEmail == Mappings.yes
    val evidenceSendDocuments = Seq("evidence.include.documents")

    var nodes = NodeSeq.Empty

    if (isEmployment1 || isEmployment2) {
      nodes ++= {
        isOriginGB match {
          case true => recipientAddress("address.send")
          case false => recipientAddressNI("address.send")
        }
      }
    }

    nodes ++= evidenceTitle("next")

    if (isEmail) nodes ++= evidenceTitle("email.circs.thankYou")

    if (isBreaksInCare) {
      nodes ++= evidenceTitle("circs.thankyou.breakmessage.content")
    }

    if (isEmployment1 || isEmployment2) {
      nodes ++= evidenceTitle("circs.next.1")

      val commonMessages = Seq("address.details")
      val employment1Pension = showEmploymentPensionMessage(circs, "circs.thankyou.employment1message3.content")
      val employment2Pension = showEmploymentPensionMessage(circs, "circs.thankyou.employment2message4.content")
      val employment1 = emptySeqIfFalse(isEmployment1, Seq("circs.thankyou.employment1message2.content") ++ employment1Pension)
      val employment2 = emptySeqIfFalse(isEmployment2, Seq("circs.thankyou.employment2message2.content", "circs.thankyou.employment2message3.content") ++ employment2Pension)

      nodes ++= evidenceSection(true, "evidence.required", Seq("thankyou.send") ++ employment1 ++ employment2 ++ evidenceSendDocuments ++ commonMessages)
    } else {
      nodes ++= evidenceTitle("circs.next.nodocuments.1")
      nodes ++= evidenceTitle(Seq("circs.next.nodocuments.2", "thankyou.report.another.change.short"))
    }

    nodes
  }

  private def title(text: String): NodeSeq = {
    <Title>{messagesApi(text)}</Title>
  }

  private def content(text: String): NodeSeq = {
    <Content>{messagesApi(text)}</Content>
  }

  def showBreaksInCaremessages(circs: Claim) = {
    val breaks = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare()).breaks
    val breaksOngoing=breaks.filter(b => b.expectToCareAgain.isDefined && b.expectToCareAgain.get.answer.equals(Some(Mappings.dontknow)))
    breaksOngoing.size >0
  }

  def showEmploymentPensionMessage(circs: Claim, message: String) = {
    lazy val pensionExpenses = circs.questionGroup[CircumstancesEmploymentPensionExpenses].getOrElse(CircumstancesEmploymentPensionExpenses())
    pensionExpenses.payIntoPension.answer match {
      case Mappings.yes => Seq(message)
      case _ => Seq.empty[String]
    }
  }
}
