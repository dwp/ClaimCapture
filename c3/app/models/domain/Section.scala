package models.domain

case class Section(id: String, questionGroups: List[QuestionGroup]) {
  def questionGroup(formId: String): Option[QuestionGroup] = {
    questionGroups.find(form => form.id == formId)
  }
}