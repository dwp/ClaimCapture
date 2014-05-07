package models.yesNo

import models.DayMonthYear
import controllers.Mappings._
import play.Logger

case class YesNoWithDateOrDateAndOptYesNoWithDateOrDate(answer: String, date1: Option[DayMonthYear] = None, date2: Option[DayMonthYear] = None, yesNoDate: OptYesNoWithDate, date3: Option[DayMonthYear] = None)

object YesNoWithDateOrDateAndOptYesNoWithDateOrDate {
  def validateDateOnYes(input: YesNoWithDateOrDateAndOptYesNoWithDateOrDate) : Boolean = {
    input.answer match {
      case `yes` => input.date1.isDefined
      case `no` => true
    }
  }

  def validateDateOnNo(input: YesNoWithDateOrDateAndOptYesNoWithDateOrDate) : Boolean = {
    input.answer match {
      case `yes` => true
      case `no` => input.date2.isDefined
    }
  }

  def validateYesNoOnYes(input: YesNoWithDateOrDateAndOptYesNoWithDateOrDate) : Boolean = {
    input.answer match {
      case `yes` => input.yesNoDate.answer.isDefined
      case `no` => true
    }
  }
}
