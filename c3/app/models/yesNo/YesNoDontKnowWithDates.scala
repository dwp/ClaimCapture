package models.yesNo

import models.DayMonthYear
import controllers.mappings.Mappings._

/**
 * Created by neddakaltcheva on 3/20/14.
 */

case class YesNoDontKnowWithDates(answer: Option[String] = None, expectStartCaringDate: Option[DayMonthYear] = None,
                                                          permanentBreakDate: Option[DayMonthYear] = None)

object YesNoDontKnowWithDates {

  def validateOnYes (input: YesNoDontKnowWithDates) : Boolean = input.answer.getOrElse("no") match {
    case `yes` => input.expectStartCaringDate.isDefined
    case _ => true
  }

  def validateOnNo (input: YesNoDontKnowWithDates) : Boolean = input.answer.getOrElse("yes") match {
    case `no` => input.permanentBreakDate.isDefined
    case _ => true
  }
}
