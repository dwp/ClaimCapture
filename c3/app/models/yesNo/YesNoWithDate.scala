package models.yesNo

import models.DayMonthYear
import controllers.Mappings._

case class YesNoWithDate(answer: String, date: Option[DayMonthYear])

object YesNoWithDate {

  def validate(input: YesNoWithDate): Boolean = {
    input.answer match {
      case `yes` => input.date.isDefined
      case `no` => true
    }
  }

  def validateAnswerNotEmpty(input: YesNoWithDate): Boolean = {
    println("validateAnswerNotEmpty " + input)
    !input.answer.isEmpty
  }

}