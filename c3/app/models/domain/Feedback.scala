package models.domain

import models.yesNo.{OptYesNoWith2Text}

case class Feedback(satisfiedAnswer: String = "", difficultyAndText: OptYesNoWith2Text = OptYesNoWith2Text(None, None, None)){
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


