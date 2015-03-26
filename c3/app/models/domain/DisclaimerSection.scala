package models.domain


case object DisclaimerSection extends Section.Identifier {
  val id = "s1"
}

case class Disclaimer(read: String = "") extends QuestionGroup(Disclaimer)

object Disclaimer extends QuestionGroup.Identifier {
  val id = s"${DisclaimerSection.id}.g1"
}



