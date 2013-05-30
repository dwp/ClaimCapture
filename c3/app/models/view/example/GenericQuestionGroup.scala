package models.view.example

class GenericQuestionGroup(var name:String, var form:Form) {

  var answered = false

  def updateForm(form:Form) = {
    this.form = form
  }
}
