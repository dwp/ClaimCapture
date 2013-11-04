package xml

import models.domain._
import app.{PensionPaymentFrequency, StatutoryPaymentFrequency}
import app.XMLValues._
import models.domain.Claim
import xml.XMLHelper._
import scala.xml.{NodeBuffer, Elem, NodeSeq}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

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
      buffer += textLine("Send us the following documents below including your Name and National Insurance (NI) number.")

      if (employed) {
        buffer += textLine("Your Employment documents.")
        buffer += textLine("Last payslip you got before your claim date: ", claimDate.dateOfClaim.`dd/MM/yyyy`)
        buffer += textLine("Any payslips you have had since then.")
        buffer += textLine("Any pension statements you may have.")

      }

      if (selfEmployed) {
        buffer += textLine("Your Self-employed documents.")
        buffer += textLine("Most recent finalised accounts you have for your business.")
        buffer += textLine("Any pension statements you may have.")
      }

      buffer += textLine("Send the above documents to:")
      buffer += textLine("CA Freepost")
      buffer += textLine("Palatine House")
      buffer += textLine("Preston")
      buffer += textLine("PR1 1HN")
      buffer += textLine("The Carer's Allowance unit will contact you if they need any further information.")
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