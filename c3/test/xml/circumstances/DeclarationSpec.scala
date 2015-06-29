package xml.circumstances

import models.view.CachedChangeOfCircs
import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, CircumstancesDeclaration}

class DeclarationSpec  extends Specification with Tags {

  val declaration = new CircumstancesDeclaration()

  "Declaration" should {

    "Generate a valid declaration section" in {
      val circs = Claim(CachedChangeOfCircs.key).update(declaration)
      val xml = Declaration.xml(circs)
      (xml \\ "DeclarationStatement" \ "Content").length mustEqual 3
      (xml \\ "DeclarationQuestion" \ "Answer").text mustEqual "Yes"
      (xml \\ "DeclarationQuestion" \ "QuestionLabel").text mustEqual "I agree"
    }.pendingUntilFixed("Pending till schema changes and modifying the code to new structure")

  } section "unit"

}
