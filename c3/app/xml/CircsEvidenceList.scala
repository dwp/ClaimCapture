package xml

import models.domain.{CircumstancesDeclaration, CircumstancesSelfEmployment, Claim}
import scala.xml.NodeSeq
import XMLHelper._
import play.api.i18n.Messages
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

object CircsEvidenceList {
  def xml(circs: Claim) = {
    <EvidenceList>
      {xmlGenerated()}{selfEmployed(circs)}{furtherInfo(circs)}
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
        buffer ++= textLine(Messages("c2.g2"))

        buffer ++= textLine(Messages("stillCaring.answer") + " = " + circsSelfEmployment.stillCaring.answer)

        circsSelfEmployment.stillCaring.answer match {
          case "no" => buffer ++= textLine(Messages("whenStoppedCaring") + " = " + circsSelfEmployment.stillCaring.date.get.`dd/MM/yyyy`)
          case _ =>
        }

        buffer ++= textLine(Messages("whenThisSelfEmploymentStarted") + " = " + circsSelfEmployment.whenThisSelfEmploymentStarted.`dd/MM/yyyy`)

        buffer ++= textLine(Messages("typeOfBusiness") + " = " + circsSelfEmployment.typeOfBusiness)
        buffer ++= textLine(Messages("totalOverWeeklyIncomeThreshold") + " = " + circsSelfEmployment.totalOverWeeklyIncomeThreshold)
      }
      case _ =>
    }

    buffer
  }

  def furtherInfo(circs: Claim): NodeSeq = {
    val declaration = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    var buffer = NodeSeq.Empty

    buffer ++= textLine(Messages("furtherinfo.title"))

    buffer ++= textLine(Messages("furtherInfoContact") + " = " + declaration.furtherInfoContact)

    buffer
  }
}
