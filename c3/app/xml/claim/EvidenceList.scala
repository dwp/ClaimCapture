package xml.claim

import models.domain._
import app.XMLValues._
import models.domain.Claim
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.MultiLineAddress
import play.api.i18n.{MMessages => Messages}

object EvidenceList {

  def buildXml(claim: Claim) = {
    <EvidenceList>
      {evidence(claim)}
    </EvidenceList>
  }

  def evidence(claim: Claim): NodeSeq = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim.toLowerCase == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim.toLowerCase == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    val evidenceEmployedStatements = Seq(Messages("evidence.employed.statement2", stringify(claimDate.dateOfClaim)), "evidence.employed.statement3", "evidence.employed.statement4")
    val evidenceSelfEmployedStatements = Seq("evidence.selfemployed.statement2", "evidence.selfemployed.statement3")

    val alwaysPrint = true

    var nodes = NodeSeq.Empty

    if (employed || selfEmployed) nodes ++= recepientAddress("s11.g5.help10")

    nodes ++= evidenceSection(employed, "evidence.employed.statement1", evidenceEmployedStatements)
    nodes ++= evidenceSection(selfEmployed, "evidence.selfemployed.statement1", evidenceSelfEmployedStatements)
    nodes ++= evidenceSection(alwaysPrint, "", Seq("evidence.statement7"))

    nodes
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

  private def recepientAddress(questionLabelCode: String):NodeSeq = {
    val address = MultiLineAddress(Some(Messages("evidence.statement3")),Some(Messages("evidence.statement4")),Some(Messages("evidence.statement5")))
    postalAddressStructureRecipientAddress(questionLabelCode, address, Some(Messages("evidence.statement6")))
  }
}