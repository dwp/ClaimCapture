package xml

import models.domain.{CircumstancesSelfEmployment, Claim}
import scala.xml.NodeSeq
import XMLHelper._
import play.api.i18n.Messages
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

object CircsEvidenceList {
  def xml(circs: Claim) = {
    <EvidenceList>
      {selfEmployed(circs)}
    </EvidenceList>
  }

  def selfEmployed(circs: Claim): NodeSeq = {
    val circsSelfEmploymentOption: Option[CircumstancesSelfEmployment] = circs.questionGroup[CircumstancesSelfEmployment]

    var buffer = NodeSeq.Empty

    circsSelfEmploymentOption match {
      case Some(circsSelfEmployment) => {
        buffer ++= textLine("XML Generated at: " + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))

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
}
