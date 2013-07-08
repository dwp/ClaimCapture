package models.domain

case class Section(id: String, questionGroups: List[QuestionGroup], visible: Boolean = true) {

  def questionGroup(questionGroup: QuestionGroup): Option[QuestionGroup] = {
    questionGroups.find(qg => qg.id == questionGroup.id)
  }

  def update(questionGroup: QuestionGroup): Section = {
    val updatedQuestionGroups = questionGroups.takeWhile(_.index < questionGroup.index) ::: List(questionGroup) ::: questionGroups.dropWhile(_.index <= questionGroup.index)
    copy(questionGroups = updatedQuestionGroups)
  }

  def delete(questionGroup: QuestionGroup): Section = {
    copy(questionGroups = questionGroups.filterNot(q => q.id == questionGroup.id))
  }

  def precedingQuestionGroups(questionGroup: QuestionGroup) = {
    questionGroups.takeWhile(_.index < questionGroup.index)
  }

  def show(): Section = {
    copy(visible = true)
  }

  def hide(): Section = {
    copy(visible = false)
  }

}

case object Section {
  def sectionID(questionGroup: QuestionGroup): String = questionGroup.id.split('.')(0)

  def index(sectionID: String): Int = sectionID.drop(1).toInt
}