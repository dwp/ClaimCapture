package models.yesNo

import models.DayMonthYear
import controllers.Mappings._

case class YesNoWithDateAndQs(answer: String = "", date: Option[DayMonthYear] = None, answer1: Option[String] = None)

object YesNoWithDateAndQs {

  def validateAddressOnYes (input: YesNoWithDateAndQs) : Boolean = input.answer match {
    case `yes` => input.answer1.isDefined
    case `no` => true
  }

  def validateDateOnNo (input: YesNoWithDateAndQs) : Boolean = input.answer match {
    case `yes` => true
    case `no` => input.date.isDefined
  }
}
