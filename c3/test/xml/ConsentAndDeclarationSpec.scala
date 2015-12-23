package xml

import models.view.CachedClaim
import org.specs2.mutable._
import models.domain.{Claim, CircumstancesDeclaration}
import utils.WithApplication
import xml.circumstances.Declaration

class ConsentAndDeclarationSpec extends Specification {
  val otherInfo = "report changes of your circumstances"

  "Consent and Declaration" should {
    val infoAgreement: String = "I agree"
    val confirmation: String = "Yes"

    "generate xml" in new WithApplication {
      val claim = Claim(CachedClaim.key).update(CircumstancesDeclaration(obtainInfoAgreement = infoAgreement,obtainInfoWhy = Some("Because I don't want")))
      val xml = Declaration.xml(claim)

      (xml \\ "Declaration" \\ "Content").text must(contain(otherInfo))
      (xml \\ "Declaration" \\ "QuestionLabel").text must contain(infoAgreement)
      (xml \\ "Declaration" \\ "Answer").text must contain(confirmation)
    }
  }
  section("unit")
}
