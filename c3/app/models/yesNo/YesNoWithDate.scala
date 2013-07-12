package models.yesNo

import models.DayMonthYear
import controllers.Mappings._

case class YesNoWithDate(answer: String, date: Option[DayMonthYear])

object YesNoWithDate {

  def validate(input: YesNoWithDate): Boolean = input.answer match {
    case `yes` => input.date.isDefined
    case _ => true
  }

  def validateAnswerNotEmpty(input: YesNoWithDate): Boolean = !input.answer.isEmpty
}