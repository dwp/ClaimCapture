package models.yesNo

import models.DayMonthYear
import controllers.Mappings._

case class YesNoWithDateAndQs(answer: String = "", date: Option[DayMonthYear] = None, answer1: Option[String] = None)

object YesNoWithDateAndQs {

  def validate(input: YesNoWithDateAndQs): Boolean = input.answer match {
    case `yes` => {
      input.date.isDefined
      input.answer1.isDefined
    }
    case _ => true
  }

  def validateNo(input: YesNoWithDateAndQs): Boolean = input.answer match {
    case `no` => {
      input.date.isDefined
      input.answer1.isDefined
    }
    case _ => true
  }

  def validateAnswerNotEmpty(input: YesNoWithDateAndQs): Boolean = !input.answer.isEmpty
}
