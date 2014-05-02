package models.yesNo

import models.DayMonthYear
import controllers.Mappings._
import play.Logger

case class YesNoWithDateAndOptYesNoWithDateOrDate(answer: String, date1: Option[DayMonthYear] = None, yesNoDate: OptYesNoWithDate, date2: Option[DayMonthYear] = None)

object YesNoWithDateAndOptYesNoWithDateOrDate {
  def validateDateOnYes(input: YesNoWithDateAndOptYesNoWithDateOrDate) : Boolean = {
    input.answer match {
      case `yes` => input.date1.isDefined
      case `no` => true
    }
  }

  def validateYesNoOnYes(input: YesNoWithDateAndOptYesNoWithDateOrDate) : Boolean = {
    input.answer match {
      case `yes` => input.yesNoDate.answer.isDefined
      case `no` => true
    }
  }
}
