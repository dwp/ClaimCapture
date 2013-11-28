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

    val evidenceStatements = Seq("evidence.statement2", "evidence.statement3", "evidence.statement4", "evidence.statement5", "evidence.statement6", "evidence.statement7")
    val evidenceEmployedStatements = Seq("evidence.employed.statement1", "evidence.employed.statement2", "evidence.employed.statement3", "evidence.employed.statement4")
    val evidenceSelfEmployedStatements = Seq("evidence.selfemployed.statement1", "evidence.selfemployed.statement2", "evidence.selfemployed.statement3")


    {evidenceSection("evidence.title1", Seq("evidence.statement1"))}

    if (employed && selfEmployed){
      //{evidenceSection("evidence.title2", evidenceEmployedStatements ++ evidenceSelfEmployedStatements ++ evidenceStatements)}
      {evidenceSection("evidence.title2", evidenceStatements)}
    } else if (employed){
      {evidenceSection("evidence.title2", evidenceEmployedStatements ++ evidenceStatements)}
    } else if (selfEmployed) {
      // TODO : Apply the claim date to the message translation
      //, stringify(claimDate.dateOfClaim))
      {evidenceSection("evidence.title2", evidenceSelfEmployedStatements ++ evidenceStatements)}
    } else {
      {evidenceSection("evidence.title2", evidenceStatements)}
    }
  }

  def  sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

  def evidenceSection (titleText: String, contents: Seq[String]): NodeSeq = {
    <Evidence>
      {title(titleText)}
      {contents map(c => content(c))}
    </Evidence>
  }

  private def title(text: String): NodeSeq = {
    <Title>{Messages(text)}</Title>
  }

  private def content(text: String): NodeSeq = {
    <Content>{Messages(text)}</Content>
  }

}