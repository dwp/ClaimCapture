package xml.circumstances

import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import models.domain.{Claim, CircumstancesDeclaration}
import utils.WithApplication

class DeclarationSpec  extends Specification {
  section("unit")
  "Declaration" should {
    "Generate a valid declaration section" in new WithApplication {
      val declaration = new CircumstancesDeclaration()
      val circs = Claim(CachedChangeOfCircs.key).update(declaration)
      val xml = Declaration.xml(circs)
      (xml \\ "DeclarationStatement" \ "Content").length mustEqual 4
      (xml \\ "DeclarationQuestion" \ "Answer").text mustEqual "Yes"
      (xml \\ "DeclarationQuestion" \ "QuestionLabel").text mustEqual "I agree"
    }
  }
  section("unit")
}
