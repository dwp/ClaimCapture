package models.yesNo

import models.DayMonthYear
import controllers.mappings.Mappings._

/**
 * Created by neddakaltcheva on 3/20/14.
 */

case class YesNoDontKnowWithDates(answer: Option[String] = None, yesdate: Option[DayMonthYear] = None,
                                  nodate: Option[DayMonthYear] = None)

object YesNoDontKnowWithDates {

  def validateOnYes(input: YesNoDontKnowWithDates): Boolean = input.answer.getOrElse("no") match {
    case `yes` => input.yesdate.isDefined
    case _ => true
  }

  def validateOnNo(input: YesNoDontKnowWithDates): Boolean = input.answer.getOrElse("yes") match {
    case `no` => input.nodate.isDefined
    case _ => true
  }
}
