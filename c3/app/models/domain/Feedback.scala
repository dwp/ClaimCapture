package models.domain

import app.ConfigProperties._
import models.yesNo.{OptYesNoWith2Text}
import org.joda.time.DateTime

case class Feedback(satisfiedAnswer: String = "", difficultyAndText: OptYesNoWith2Text = OptYesNoWith2Text(None, None, None)){
  def jsonmap:Map[String,String]={
    Map(
      "origin"->getProperty("origin.tag","GB"),
      "datetime"->(DateTime.now.getMillis/1000).toString,
      "satisfied"->satisfiedAnswer,
      "difficult"->difficultyAndText.answer.getOrElse(""),
      "comment"->difficultyAndText.text)
  }
}

object Feedback {
  val id = "satisfied"
}

object SatisfiedOptions {
  val id = "satisfied"
  val vs = "VS"
  val s = "S"
  val neither = "NEITHER"
  val d = "D"
  val vd = "VD"
}


