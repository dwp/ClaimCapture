package models.domain

import models.yesNo.YesNoWithText

object ConsentAndDeclaration extends Identifier(id = "s12")

case class Declaration(informationFromPerson: YesNoWithText = YesNoWithText(answer="", text = None), jsEnabled: Boolean = false) extends QuestionGroup(Declaration)

object Declaration extends QGIdentifier(id = s"${ConsentAndDeclaration.id}.g4")

object Submit extends QGIdentifier(id = s"${ConsentAndDeclaration.id}.g5")

object Error extends QGIdentifier(id = s"${ConsentAndDeclaration.id}.g6")

