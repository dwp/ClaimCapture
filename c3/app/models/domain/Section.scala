package models.domain

case class Section(id: String, questionGroups: List[QuestionGroup]) {
  def questionGroup(questionGroup: QuestionGroup): Option[QuestionGroup] = {
    questionGroups.find(qg => qg.id == questionGroup.id)
  }
}

case object Section {
  def sectionID(questionGroup: QuestionGroup): String = questionGroup.id.split('.')(0)
}