package models.yesNo

import models.DayMonthYear
import controllers.mappings.Mappings._

case class OptYesNoWithDate(answer: Option[String], date: Option[DayMonthYear])

object OptYesNoWithDate {

  def validateOnYes(input: OptYesNoWithDate): Boolean = input.answer.getOrElse("no") match {
    case `yes` => input.date.isDefined
    case _ => true
  }

  def validateNo(input: OptYesNoWithDate): Boolean = input.answer.getOrElse("yes") match {
      case `no` => input.date.isDefined
      case _ => true
    }

  /**
   * we are using this function in scenarios where the answer is yes and the field to be validated
   * is not visible based on some other condition
   * @param input
   * @return
   */
  def doNotValidateOnYes(input: OptYesNoWithDate): Boolean = input.answer match {
    case _ => true
  }
}
