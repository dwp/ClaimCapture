package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, CircumstancesDeclaration}

class ConsentAndDeclarationSpec extends Specification with Tags {
  val otherInfo = "Some other info"

  "Consent and Declaration" should {
    val infoAgreement: String = "no"
    val confirmation: String = "yes"

    "generate xml" in {
      val claim = Claim().update(CircumstancesDeclaration(obtainInfoAgreement = infoAgreement,obtainInfoWhy = Some("Because I don't want"), confirm = confirmation))
      val xml = ConsentAndDeclaration.xml(claim)

      (xml \\ "Declaration" \\ "TextLine").theSeq(6).text.toLowerCase must(contain(infoAgreement))
      (xml \\ "Declaration" \\ "TextLine").text.toLowerCase must contain(confirmation)
    }

  } section "unit"
}