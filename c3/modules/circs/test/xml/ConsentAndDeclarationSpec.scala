package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{CircumstancesDeclaration, CircumstancesOtherInfo, Circs}

class ConsentAndDeclarationSpec extends Specification with Tags {

  val otherInfo = "Some other info"

  "Consent and Declaration" should {
    val infoAgreement: String = "no"
    val confirmation: String = "yes"

    "generate xml" in {
      val claim = Circs()().update(CircumstancesDeclaration(obtainInfoAgreement = infoAgreement,obtainInfoWhy = Some("Because I don't want"), confirm = confirmation))
      val xml = ConsentAndDeclaration.xml(claim.asInstanceOf[Circs])

      (xml \\ "Declaration" \\ "TextLine").theSeq(4).text must(contain(infoAgreement))
      (xml \\ "EvidenceList" \\ "TextLine").text must contain(confirmation)
    }

  } section "unit"
}