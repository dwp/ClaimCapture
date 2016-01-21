package models.domain

import models.yesNo.YesNoWithText

object ConsentAndDeclaration extends Section.Identifier {
  val id = "s12"
}

case class Declaration(informationFromPerson: YesNoWithText = YesNoWithText(answer="", text = None), jsEnabled: Boolean = false) extends QuestionGroup(Declaration)

object Declaration extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g4"
}

object Submit extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g5"
}

object Error extends QuestionGroup.Identifier {
  val id = s"${ConsentAndDeclaration.id}.g6"
}
