package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, CircumstancesDeclaration}
import xml.circumstances.Declaration

class ConsentAndDeclarationSpec extends Specification with Tags {
  val otherInfo = "Some other info"

  "Consent and Declaration" should {
    val infoAgreement: String = "no"
    val confirmation: String = "yes"

    "generate xml" in {
      val claim = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = infoAgreement,obtainInfoWhy = Some("Because I don't want"), confirm = confirmation))
      val xml = Declaration.xml(claim)

      (xml \\ "Declaration" \\ "TextLine").theSeq(6).text must(contain(infoAgreement))
      (xml \\ "EvidenceList" \\ "TextLine").text must contain(confirmation)
    }.pendingUntilFixed("Schema changes: Needs to implement the new Declaration and Evidence structure")

  } section "unit"
}