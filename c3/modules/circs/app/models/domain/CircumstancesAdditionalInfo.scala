package models.domain

case object CircumstancesAdditionalInfo extends Section.Identifier {
  val id = "c2"
}

case class CircumstancesOtherInfo(change: String = "") extends QuestionGroup(CircumstancesOtherInfo){
  override def numberOfCharactersInput: Int = change.length
}

object CircumstancesOtherInfo extends QuestionGroup.Identifier {
  val id = s"${CircumstancesAdditionalInfo.id}.g2"
}
