package models.view

abstract class Section {
  var complete = false
  def questionGroups : Seq[QuestionGroup]
  def name : String
}
