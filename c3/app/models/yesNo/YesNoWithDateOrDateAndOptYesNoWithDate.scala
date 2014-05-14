package models.yesNo

import models.DayMonthYear
import controllers.Mappings._
import play.Logger

case class YesNoWithDateOrDateAndOptYesNoWithDate(answer: String, date1: Option[DayMonthYear] = None, date2: Option[DayMonthYear] = None, yesNoDate: OptYesNoWithDate)

object YesNoWithDateOrDateAndOptYesNoWithDate {
  def validateDateOnYes(input: YesNoWithDateOrDateAndOptYesNoWithDate) : Boolean = {
    input.answer match {
      case `yes` => input.date1.isDefined
      case `no` => true
    }
  }

  def validateDateOnNo(input: YesNoWithDateOrDateAndOptYesNoWithDate) : Boolean = {
    input.answer match {
      case `yes` => true
      case `no` => input.date2.isDefined
    }
  }

  def validateYesNoOnYes(input: YesNoWithDateOrDateAndOptYesNoWithDate) : Boolean = {
    input.answer match {
      case `yes` => input.yesNoDate.answer.isDefined
      case `no` => true
    }
  }
}
