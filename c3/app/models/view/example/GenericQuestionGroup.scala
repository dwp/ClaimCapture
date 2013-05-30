package models.view.example

class GenericQuestionGroup(_name:String, _form:Form) {

  var answered = false
  var form:Form = _form;

  def name: String = {
    _name
  }

  def updateForm(form:Form) = {
    this.form = form
  }
}
