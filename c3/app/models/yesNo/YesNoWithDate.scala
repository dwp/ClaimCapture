package models.yesNo

import models.DayMonthYear
import controllers.mappings.Mappings._

case class YesNoWithDate(answer: String, date: Option[DayMonthYear])

object YesNoWithDate {

  def validate(input: YesNoWithDate): Boolean = input.answer match {
    case `yes` => input.date.isDefined
    case _ => true
  }

  def validateNo(input: YesNoWithDate): Boolean = input.answer match {
    case `no` => input.date.isDefined
    case _ => true
  }

  def validateAnswerNotEmpty(input: YesNoWithDate): Boolean = !input.answer.isEmpty
}
