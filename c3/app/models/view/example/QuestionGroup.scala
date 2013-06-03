package models.view.example

class QuestionGroup(val label:String, var form:AbstractForm) {

  var answered:Boolean = false

  def updateForm(form:AbstractForm) {
    this.form = form
  }

}
