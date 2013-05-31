package models.view.example

class GenericSection(var name:String, var questionGroups: Seq[GenericQuestionGroup]) {
  var complete = false

  def getNextUnansweredQuestionGroup():Option[GenericQuestionGroup] = {
    questionGroups.find(questionGroup => !questionGroup.answered)
  }

  def getAnsweredQuestionGroups():Seq[GenericQuestionGroup] = {
    questionGroups.filter(questionGroup => questionGroup.answered)
  }

}
