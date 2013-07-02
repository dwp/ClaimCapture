package models.domain

case class Section(id: String, questionGroups: List[QuestionGroup]) {
  def questionGroup(questionGroupID: String): Option[QuestionGroup] = {
    questionGroups.find(qg => qg.id == questionGroupID)
  }
}

object Section {
  def sectionID(questionGroupID: String) = {
    questionGroupID.split('.')(0)
  }
}