package models.view


abstract class QuestionGroup {
  def form : CarersForm
  def name : String
  var answered = false
}
