package xml.circumstances

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, CircumstancesDeclaration}

class DeclarationSpec  extends Specification with Tags {

  val declaration = new CircumstancesDeclaration(confirm = "Yes")

  "Declaration" should {

    "Generate a valid declaration section" in {
      val circs = Claim().update(declaration)
      val xml = Declaration.xml(circs)
      (xml \\ "DeclarationStatement" \ "Content").length mustEqual 3
      (xml \\ "DeclarationQuestion" \ "Answer").text mustEqual declaration.confirm
      (xml \\ "DeclarationQuestion" \ "QuestionLabel").text mustEqual "confirm"
    }.pendingUntilFixed("Pending till schema changes and modifying the code to new structure")

  } section "unit"

}
