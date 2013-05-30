package models.view.example

class GenericSection(_name:String, _questionGroups: Seq[GenericQuestionGroup]) {
  var complete = false

  def name: String = {
    _name
  }

  def questionGroups : Seq[GenericQuestionGroup] = {
     _questionGroups
  }


}
