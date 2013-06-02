package models.view.trash

abstract class QuestionGroup {
  def form : CarersForm
  def name : String
  var answered = false
}
