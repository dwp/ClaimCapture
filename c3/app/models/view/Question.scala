package models.view

abstract class Question {
  val label:String
  var value:Option[String] = None
}

