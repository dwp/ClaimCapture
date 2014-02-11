package xml

import models.domain.{CircumstancesReportChange, CircumstancesDeclaration, CircumstancesSelfEmployment, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._
import play.api.i18n.Messages
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import models.domain.Claim
import scala.Some

object CircsEvidenceList {
  def xml(circs: Claim) = {
    <EvidenceList>
      {xmlGenerated()}{selfEmployed(circs)}{furtherInfo(circs)}{theirInfo(circs)}
    </EvidenceList>
  }

  def xmlGenerated() = {
    textLine("XML Generated at: " + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
  }

  def selfEmployed(circs: Claim): NodeSeq = {
    val circsSelfEmploymentOption: Option[CircumstancesSelfEmployment] = circs.questionGroup[CircumstancesSelfEmployment]

    var buffer = NodeSeq.Empty

    circsSelfEmploymentOption match {
      case Some(circsSelfEmployment) => {
        buffer ++=  textSeparatorLine(Messages("c2.g2"))

        buffer ++= textLine(Messages("stillCaring.answer") + " = " + circsSelfEmployment.stillCaring.answer)

        circsSelfEmployment.stillCaring.answer match {
          case "no" => buffer ++= textLine(Messages("whenStoppedCaring") + " = " + circsSelfEmployment.stillCaring.date.get.`yyyy-MM-dd`)
          case _ =>
        }

        buffer ++= textLine(Messages("whenThisSelfEmploymentStarted") + " = " + circsSelfEmployment.whenThisSelfEmploymentStarted.`yyyy-MM-dd`)

        buffer ++= textLine(Messages("typeOfBusiness") + " = " + circsSelfEmployment.typeOfBusiness)
        buffer ++= textLine(Messages("totalOverWeeklyIncomeThreshold") + " = " + circsSelfEmployment.totalOverWeeklyIncomeThreshold)
      }
      case _ =>
    }

    buffer
  }

  def furtherInfo(circs: Claim): NodeSeq = {
    val declaration = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    var buffer = NodeSeq.Empty ++ textSeparatorLine(Messages("furtherinfo.title"))

    buffer ++= textLine(Messages("furtherInfoContact") + " = " + declaration.furtherInfoContact)

    buffer
  }

  def theirInfo(circs: Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    var buffer = NodeSeq.Empty ++ textSeparatorLine(Messages("c1.personYouAreCaringFor"))

    buffer ++= textLine(Messages("theirRelationshipToYou") + " = " + reportChange.theirRelationshipToYou)

    buffer
  }
}
