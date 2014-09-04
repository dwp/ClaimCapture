package models.yesNo

import models.DayMonthYear
import controllers.Mappings._

case class YesNoWithMutuallyExclusiveDates(answer: String, date1: Option[DayMonthYear] = None, date2: Option[DayMonthYear] = None)

object YesNoWithMutuallyExclusiveDates {
  def validateDateOnYes(input: YesNoWithMutuallyExclusiveDates) : Boolean = {
    input.answer match {
      case `yes` => input.date1.isDefined
      case `no` => true
    }
  }

  def validateDateOnNo(input: YesNoWithMutuallyExclusiveDates) : Boolean = {
    input.answer match {
      case `yes` => true
      case `no` => input.date2.isDefined
    }
  }
//
//  def validateYesNoOnYes(input: YesNoWithMutuallyExclusiveDates) : Boolean = {
//    input.answer match {
//      case `yes` => input.yesNoDate.answer.isDefined
//      case `no` => true
//    }
//  }
}
