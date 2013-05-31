package models.view.example

class GenericSection(var name:String, var questionGroups: Seq[GenericQuestionGroup]) {

  def isComplete = {
    getNextUnansweredQuestionGroup().isEmpty
  }

  def getNextUnansweredQuestionGroup():Option[GenericQuestionGroup] = {
    questionGroups.find(questionGroup => !questionGroup.answered)
  }

  def getAnsweredQuestionGroups():Seq[GenericQuestionGroup] = {
    questionGroups.filter(questionGroup => questionGroup.answered)
  }

}
