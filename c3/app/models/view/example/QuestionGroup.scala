package models.view.example

class QuestionGroup(var label:String, var form:AbstractForm) {

  var answered:Boolean = false

  def updateForm(form:AbstractForm) {
    this.form = form
  }

}
