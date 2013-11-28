package xml.claim

import models.domain._
import app.XMLValues._
import models.domain.Claim
import scala.xml.NodeSeq
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

  def evidence(claim: Claim): NodeSeq = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim.toLowerCase == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim.toLowerCase == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val alwaysPrint = true

    val evidenceInitialStatements = Seq("evidence.title2", "evidence.statement1")
    val evidenceAddressStatements = Seq("evidence.statement3", "evidence.statement4", "evidence.statement5", "evidence.statement6", "evidence.statement7")
    val evidenceEmployedStatements = Seq(Messages("evidence.employed.statement2", stringify(claimDate.dateOfClaim)), "evidence.employed.statement3", "evidence.employed.statement4")
    val evidenceSelfEmployedStatements = Seq("evidence.selfemployed.statement2", "evidence.selfemployed.statement3")

    evidenceSection(alwaysPrint, "evidence.title1", evidenceInitialStatements)++
    evidenceSection(employed, "evidence.employed.statement1", evidenceEmployedStatements)++
    evidenceSection(selfEmployed, "evidence.selfemployed.statement1", evidenceSelfEmployedStatements)
  }

  def  sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

  def evidenceSection (condition: Boolean, titleText: String, contents: Seq[String]): NodeSeq = {
    condition match {
      case true =>{
        <Evidence>
          {title(titleText)}
          {contents map(c => content(c))}
        </Evidence>
      }
      case _ => NodeSeq.Empty
    }
  }

  private def title(text: String): NodeSeq = {
    <Title>{Messages(text)}</Title>
  }

  private def content(text: String): NodeSeq = {
    <Content>{Messages(text)}</Content>
  }

}