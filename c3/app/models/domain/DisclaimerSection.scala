package models.domain

object DisclaimerSection extends Identifier(id = "s1")

case class Disclaimer(read: String = "") extends QuestionGroup(Disclaimer)

object Disclaimer extends QGIdentifier(id = s"${DisclaimerSection.id}.g1")




