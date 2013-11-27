package xml.claim

import models.domain._
import app.{PensionPaymentFrequency, StatutoryPaymentFrequency}
import app.XMLValues._
import models.domain.Claim
import xml.XMLHelper._
import scala.xml.{NodeBuffer, Elem, NodeSeq, Node}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import play.api.i18n.Messages
import xml.XMLHelper._

object EvidenceList {

  def buildXml(claim: Claim) = {
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())

    <EvidenceList>
      {postalAddressStructureRecipientAddress(theirContactDetails.address, theirContactDetails.postcode)}
      {evidence(claim)}
    </EvidenceList>
  }
//
//  def xmlGenerated() = {
//    textLine("XML Generated at: "+DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
//  }


  def evidence(claim: Claim): NodeSeq = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim.toLowerCase == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim.toLowerCase == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val buffer = new NodeBuffer

    if (employed || selfEmployed) {
      evidence("evidence.title1", Messages("evidence.statement1")) ++
      <Evidence>
       <Title>{Messages("evidence.title2")}</Title>

      {
      // TODO : Confirm that what we're doing here actually works, ie that I get the evidence lines that I expect
      }

      {if (employed) {
        content("evidence.employed.statement1")
        content("evidence.employed.statement2")
        // TODO : Sort out what to do about this date, do we actually need it?
      //, stringify(claimDate.dateOfClaim))
        content("evidence.employed.statement3")
        content("evidence.employed.statement4")
        }
      }

      {if (selfEmployed) {
        content("evidence.selfemployed.statement1")
        content("evidence.selfemployed.statement2")
        content("evidence.selfemployed.statement3")
        }
      }

      {
      content("evidence.statement2")
      content("evidence.statement3")
      content("evidence.statement4")
      content("evidence.statement5")
      content("evidence.statement6")
      content("evidence.statement7")
      }
    </Evidence>
  }

  def sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

    // TODO : Make this a private def
    def content(text: String): NodeSeq = {
      <Content>{Messages(text)}</Content>
    }
//  private def textLine(text: String): NodeSeq = <Evidence>
//    <Title></Title>
//    <Content>{Messages(text)}</Content>
//  </Evidence>

  private def evidence(label: String, value: String): NodeSeq  = {
    <Evidence>
      <Title>{Messages(label)}</Title>
      <Content>{value}</Content>
    </Evidence>
  }

//  private def textLine(label: String, value: Option[String]): Elem = value match {
//    case Some(s) => textLine(label, value.getOrElse(""))
//    case None => <Evidence/>
//  }
}