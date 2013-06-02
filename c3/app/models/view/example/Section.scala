package models.view.example

case class Section(name: String, questionGroups: Seq[QuestionGroup]) {

  def isComplete = {
    getNextUnansweredQuestionGroup.isEmpty
  }

  def getNextUnansweredQuestionGroup: Option[QuestionGroup] = {
    questionGroups.find(questionGroup => !questionGroup.answered)
  }

  def getAnsweredQuestionGroups: Seq[QuestionGroup] = {
    questionGroups.filter(questionGroup => questionGroup.answered)
  }

}
