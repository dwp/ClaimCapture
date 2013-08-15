package xml

import org.specs2.mutable.{Tags, Specification}
import controllers.Mappings.yes
import models.domain.{Claim}

class EvidenceListSpec extends Specification with Tags {

  val employmentDocuments = "Your Employment documents"
  val selfEmploymentDocuments= "Your Self-employed documents"

  "EvidenceList" should {

    "generate required documents text" in {

      "when employed" in {
        val employment = models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = yes)
        val claim = Claim().update(employment)
        val xml = EvidenceList.xml(claim)

        xml.text must contain(employmentDocuments)
        xml.text must not(contain(selfEmploymentDocuments))
      }

      "when self employed" in {
        val employment = models.domain.Employment(beenSelfEmployedSince1WeekBeforeClaim = yes)
        val claim = Claim().update(employment)
        val xml = EvidenceList.xml(claim)
        xml.text must contain(selfEmploymentDocuments)
        xml.text must not(contain(employmentDocuments))
      }

      "when both employed and self employed" in {
        val employment = models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = yes, beenSelfEmployedSince1WeekBeforeClaim = yes)
        val claim = Claim().update(employment)
        val xml = EvidenceList.xml(claim)
        xml.text must contain(employmentDocuments)
        xml.text must contain(selfEmploymentDocuments)
      }
    }
  } section "unit"

}
