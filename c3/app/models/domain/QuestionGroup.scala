package models.domain

abstract class QuestionGroup(val id: String) {
  val index: Int = id.dropWhile(!_.equals('g')).drop(1).toInt
}