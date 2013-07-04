package models.domain

case class Section(id: String, questionGroups: List[QuestionGroup]) {
  def questionGroup(questionGroup: QuestionGroup): Option[QuestionGroup] = {
    questionGroups.find(qg => qg.id == questionGroup.id)
  }
}

object Section {
  def sectionID(questionGroupID: String) = {
    questionGroupID.split('.')(0)
  }
}