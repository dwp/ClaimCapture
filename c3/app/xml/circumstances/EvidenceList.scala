package xml.circumstances

import controllers.mappings.Mappings
import models.domain._
import app.XMLValues._
import models.domain.Claim
import scala.xml.NodeSeq
import play.api.i18n.{MMessages => Messages}

object EvidenceList {

  def buildXml(circs: Claim) = {
    <EvidenceList>
      {evidence(circs)}
    </EvidenceList>
  }

  def evidence(circs:Claim):NodeSeq = {
    import xml.claim.EvidenceList._

    val isBreaksInCare = showBreaksInCareMessages(circs)
    val isEmployment1 = circs.questionGroup[CircumstancesStartedEmploymentAndOngoing].isDefined
    val isEmployment2 = circs.questionGroup[CircumstancesStartedAndFinishedEmployment].isDefined
    val isEmail = circs.questionGroup[ContactDetails].getOrElse(ContactDetails()).wantsContactEmail.getOrElse("") == Mappings.yes

    var nodes = NodeSeq.Empty

    if(isEmployment1 || isEmployment2){
      nodes ++= recepientAddress("address.send")
    }

    nodes ++= evidenceTitle("next")

    if (isEmail) nodes ++= evidenceTitle("email.circs.thankYou")

    if(isBreaksInCare){
      nodes ++= evidenceTitle("circs.thankyou.breakmessage.content.nohtml")
    }

    if(isEmployment1 || isEmployment2){
      nodes ++= evidenceTitle("circs.next.1")

      val commonMessages = Seq("address.details")
      val employment1Pension = showEmploymentPensionMessage(circs, "circs.thankyou.employment1message3.content")
      val employment2Pension = showEmploymentPensionMessage(circs, "circs.thankyou.employment2message4.content")
      val employment1 = emptySeqIfFalse(isEmployment1, Seq("circs.thankyou.employment1message2.content") ++ employment1Pension)
      val employment2 = emptySeqIfFalse(isEmployment2, Seq("circs.thankyou.employment2message2.content", "circs.thankyou.employment2message3.content") ++ employment2Pension)

      nodes ++= evidenceSection(true, "evidence.required", Seq("thankyou.send") ++ employment1 ++ employment2 ++ commonMessages)

    }else{
      nodes ++= evidenceTitle("circs.next.nodocuments.1")
      nodes ++= evidenceTitle(Messages("circs.next.nodocuments.2")+" "+Messages("thankyou.report.another.change.short"))
    }

    nodes
  }

  private def title(text: String): NodeSeq = {
    <Title>{Messages(text)}</Title>
  }

  private def content(text: String): NodeSeq = {
    <Content>{Messages(text)}</Content>
  }

  def showBreaksInCareMessages(circs: Claim) = {
    val breaksIncare = circs.questionGroup[CircumstancesBreaksInCare].getOrElse(CircumstancesBreaksInCare())
    val outputRequired = breaksIncare.expectStartCaring.answer match {
      case Some(n) => n match {
        case Mappings.yes => if(!breaksIncare.expectStartCaring.expectStartCaringDate.isDefined) true else false
        case Mappings.dontknow => true
        case _ => false
      }
      case _ => false
    }

    outputRequired
  }

  def showEmploymentPensionMessage(circs: Claim,message:String) = {
    lazy val ongoingEmploymentOption = circs.questionGroup[CircumstancesStartedEmploymentAndOngoing]
    lazy val finishedEmploymentOption = circs.questionGroup[CircumstancesStartedAndFinishedEmployment]
    lazy val futureEmploymentOption = circs.questionGroup[CircumstancesEmploymentNotStarted]

    val show =
      if (ongoingEmploymentOption.isDefined)
        ongoingEmploymentOption.get.payIntoPension.answer == yes
      else if (finishedEmploymentOption.isDefined)
        finishedEmploymentOption.get.payIntoPension.answer == yes
      else if (futureEmploymentOption.isDefined)
        futureEmploymentOption.get.payIntoPension.answer == yes
      else false

    show match {
      case true => Seq(message)
      case _ => Seq.empty[String]
    }
  }
}