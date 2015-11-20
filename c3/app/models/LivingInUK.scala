package models

import yesNo.YesNoWithDate
import controllers.mappings.Mappings._

case class LivingInUK(answer: String = "",
                      date: Option[DayMonthYear] = None,
                      text: Option[String] = None,
                      goBack: Option[YesNoWithDate] = None)

object LivingInUK {

  def validateDate(input: LivingInUK): Boolean = input.answer match {
    case `yes` => input.date.isDefined
    case `no` => true
  }

  def validateGoBack(input: LivingInUK): Boolean = input.answer match {
    case `yes` => input.goBack.isDefined
    case `no` => true
  }
}
