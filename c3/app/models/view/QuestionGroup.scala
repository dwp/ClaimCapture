package models.view


abstract class QuestionGroup {
  var answered = false
  def questions : Seq[Question]
}
