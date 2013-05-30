package models.view.example

class GenericSection(var name:String, var questionGroups: Seq[GenericQuestionGroup]) {
  var complete = false
}
