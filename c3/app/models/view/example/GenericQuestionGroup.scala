package models.view.example

class GenericQuestionGroup(var name:String, var form:AbstractForm) {

  var answered:Boolean = false

  def updateForm(form:AbstractForm) = {
    this.form = form
  }
}
