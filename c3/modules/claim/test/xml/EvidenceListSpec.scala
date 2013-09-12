package xml

import org.specs2.mutable.{Tags, Specification}
import controllers.Mappings.yes
import models.domain.{SelfEmploymentYourAccounts, Claim}
import scala.xml.NodeSeq

class EvidenceListSpec extends Specification with Tags {

  val employmentDocuments = "Your Employment documents"
  val selfEmploymentDocuments= "Your Self-employed documents"

  "EvidenceList" should {

    "generate required documents text" in {

      "when employed" in {
        val employment = models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = yes)
        val claim = Claim()().update(employment)
        val xml = EvidenceList.xml(claim.asInstanceOf[Claim])

        xml.text must contain(employmentDocuments)
        xml.text must not(contain(selfEmploymentDocuments))
      }

      "when self employed" in {
        val employment = models.domain.Employment(beenSelfEmployedSince1WeekBeforeClaim = yes)
        val claim = Claim()().update(employment)
        val xml = EvidenceList.xml(claim.asInstanceOf[Claim])

        xml.text must contain(selfEmploymentDocuments)
        xml.text must not(contain(employmentDocuments))
      }

      "when both employed and self employed" in {
        val employment = models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = yes, beenSelfEmployedSince1WeekBeforeClaim = yes)
        val claim = Claim()().update(employment)
        val xml = EvidenceList.xml(claim.asInstanceOf[Claim])
        xml.text must contain(employmentDocuments)
        xml.text must contain(selfEmploymentDocuments)
      }

    }

    "generate self employment section when something answered" in {
      val yourAccounts = SelfEmploymentYourAccounts(areIncomeOutgoingsProfitSimilarToTrading = Some("true") )
      val claim = Claim()().update(yourAccounts)
      val xml = EvidenceList.selfEmployment(claim.asInstanceOf[Claim])

      xml.text must not(beEmpty)
    }

    "skip self employment section when nothing answered" in {
      val xml = EvidenceList.selfEmployment(Claim()())

      xml.text must beEmpty
    }

    "return true for sectionEmpty " in {
      "when NodeSeq is empty" in {
        EvidenceList.sectionEmpty(NodeSeq.Empty) must beTrue
      }
      "when NodeSeq is null" in {
        EvidenceList.sectionEmpty(null) must beTrue
      }
      "when NodeSeq.text is empty" in {

        EvidenceList.sectionEmpty(<TextLine/>) must beTrue
      }
    }

    "return false for sectionEmpty" in {
       "when contains nodes contain text" in {
         EvidenceList.sectionEmpty(<TextLine>text</TextLine>) must beFalse
       }
    }

    "generate a correct section seperator" in {
      val sep = EvidenceList.textSeparatorLine("word")
      sep.text.trim() must beEqualTo("=========================word=========================")
    }
  } section "unit"
}