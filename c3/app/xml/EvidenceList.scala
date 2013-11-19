package xml

import models.domain._
import app.{PensionPaymentFrequency, StatutoryPaymentFrequency}
import app.XMLValues._
import models.domain.Claim
import xml.XMLHelper._
import scala.xml.{NodeBuffer, Elem, NodeSeq}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import play.api.i18n.Messages

object EvidenceList {

  def buildXml(claim: Claim) = {
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())

    <EvidenceList>
      {xml.XMLHelper.postalAddressStructureRecipientAddress(theirContactDetails.address, theirContactDetails.postcode.orNull)}
      {xmlGenerated()}
      {evidence(claim)}
    </EvidenceList>
  }

  def xmlGenerated() = {
    textLine("XML Generated at: "+DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
  }


  def evidence(claim: Claim): NodeBuffer = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    val buffer = new NodeBuffer

    if (employed || selfEmployed) {
      buffer += textLine({Messages("evidence.title1")})
      buffer += textLine({Messages("evidence.statement1")})
      buffer += textLine({Messages("evidence.title2")})

      if (employed) {
        buffer += textLine({Messages("evidence.employed.statement1")})
        buffer += textLine({Messages("evidence.employed.statement2")}, claimDate.dateOfClaim.`dd/MM/yyyy`)
        buffer += textLine({Messages("evidence.employed.statement3")})
        buffer += textLine({Messages("evidence.employed.statement4")})

      }

      if (selfEmployed) {
        buffer += textLine({Messages("evidence.selfemployed.statement1")})
        buffer += textLine({Messages("evidence.selfemployed.statement2")})
        buffer += textLine({Messages("evidence.selfemployed.statement3")})
      }

      buffer += textLine({Messages("evidence.statement2")})
      buffer += textLine({Messages("evidence.statement3")})
      buffer += textLine({Messages("evidence.statement4")})
      buffer += textLine({Messages("evidence.statement5")})
      buffer += textLine({Messages("evidence.statement6")})
      buffer += textLine({Messages("evidence.statement7")})
    }

    buffer
  }

  def sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

  private def textLine(text: String): Elem = <Evidence>
    <Title></Title>
    <Content>{text}</Content>
  </Evidence>

  private def textLine(label: String, value: String): Elem = {
    <Evidence>
      <Title>{label}</Title>
      <Content>{formatValue(value)}</Content>
    </Evidence>
  }

  private def textLine(label: String, value: Option[String]): Elem = value match {
    case Some(s) => textLine(label, value.getOrElse(""))
    case None => <Evidence/>
  }
}