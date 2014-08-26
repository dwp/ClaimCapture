package xml.circumstances

import controllers.Mappings
import models.domain._
import app.XMLValues._
import models.domain.Claim
import play.api.Logger
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.MultiLineAddress
import play.api.i18n.{MMessages => Messages}

object EvidenceList {

  def buildXml(circs: Claim) = {
    <EvidenceList>
      {showBreaksInCareMessages(circs)}
      {showEmployment1Messages(circs)}
      {showEmployment2Messages(circs)}
    </EvidenceList>
  }

  private def title(text: String): NodeSeq = {
    <Title>{Messages(text)}</Title>
  }

  private def content(text: String): NodeSeq = {
    <Content>{Messages(text)}</Content>
  }

  def showBreaksInCareMessages(circs: Claim) = {
    var breaksIncare = circs.questionGroup[CircumstancesBreaksInCare].getOrElse(CircumstancesBreaksInCare())
    var outputRequired = breaksIncare.expectStartCaring.answer match {
      case Some(n) => n match {
        case Mappings.yes => if(!breaksIncare.expectStartCaring.expectStartCaringDate.isDefined) true else false
        case Mappings.dontknow => true
        case _ => false
      }
      case _ => false
    }

    outputRequired match {
      case true => {
        <Evidence>
          {title(Messages("circs.thankyou.breakmessage.title"))}
          {content(Messages("circs.thankyou.breakmessage.content"))}
        </Evidence>
      }
      case _ => NodeSeq.Empty
    }
  }

  def showEmployment1Messages(circs: Claim) = {
    circs.questionGroup[CircumstancesStartedEmploymentAndOngoing].isDefined match {
      case true => {
        <Evidence>
          {title(Messages("circs.thankyou.employment1message.title"))}
          {content(Messages("circs.thankyou.employment1message1.content"))}
          {content(Messages("circs.thankyou.employment1message2.content"))}
          {showEmploymentPensionMessage(circs)}
          {content(Messages("circs.thankyou.employment1message.address.title"))}
          {content(Messages("circs.thankyou.employment1message1.address"))}
          {content(Messages("circs.thankyou.employment1message2.address"))}
          {content(Messages("circs.thankyou.employment1message3.address"))}
          {content(Messages("circs.thankyou.employment1message4.address"))}
        </Evidence>
      }
      case _ => NodeSeq.Empty
    }
  }

  def showEmployment2Messages(circs: Claim) = {
    circs.questionGroup[CircumstancesStartedAndFinishedEmployment].isDefined match {
      case true => {
        <Evidence>
          {title(Messages("circs.thankyou.employment2message.title"))}
          {content(Messages("circs.thankyou.employment2message1.content"))}
          {content(Messages("circs.thankyou.employment2message2.content"))}
          {content(Messages("circs.thankyou.employment2message3.content"))}
          {showEmploymentPensionMessage(circs)}
          {content(Messages("circs.thankyou.employment2message.address.title"))}
          {content(Messages("circs.thankyou.employment2message1.address"))}
          {content(Messages("circs.thankyou.employment2message2.address"))}
          {content(Messages("circs.thankyou.employment2message3.address"))}
          {content(Messages("circs.thankyou.employment2message4.address"))}
        </Evidence>
      }
      case _ => NodeSeq.Empty
    }
  }

  def showEmploymentPensionMessage(circs: Claim) = {
    lazy val ongoingEmploymentOption = circs.questionGroup[CircumstancesStartedEmploymentAndOngoing]
    lazy val finishedEmploymentOption = circs.questionGroup[CircumstancesStartedAndFinishedEmployment]
    lazy val futureEmploymentOption = circs.questionGroup[CircumstancesEmploymentNotStarted]

    val show =
      if (ongoingEmploymentOption.isDefined)
        ongoingEmploymentOption.get.payIntoPension.answer == "yes"
      else if (finishedEmploymentOption.isDefined)
        finishedEmploymentOption.get.payIntoPension.answer == "yes"
      else if (futureEmploymentOption.isDefined)
        futureEmploymentOption.get.payIntoPension.answer == "yes"
      else false

    show match {
      case true => content(Messages("circs.thankyou.employment1message3.content"))
      case _ => NodeSeq.Empty
    }
  }
}