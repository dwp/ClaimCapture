package models.domain

import app.ConfigProperties._
import models.yesNo.{OptYesNoWith2Text}
import org.joda.time.DateTime

case class Feedback(satisfiedAnswer: String = "",
                    difficulty: Option[String]=None,
                    feedback_text1: Option[String] = None,
                    feedback_text2: Option[String] = None ){
  def datetimesecs = DateTime.now.getMillis / 1000

  def origin = getStringProperty("origin.tag")

  def satisfiedScore = {
    satisfiedAnswer match {
      case "VS" => 5
      case "S" => 4
      case "NEITHER" => 3
      case "D" => 2
      case "VD" => 1
    }
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


