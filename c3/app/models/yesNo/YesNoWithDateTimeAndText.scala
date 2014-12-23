package models.yesNo

import models.DayMonthYear
import controllers.mappings.Mappings._

/**
 * Created by neddakaltcheva on 3/20/14.
 */

case class YesNoWithDateTimeAndText(answer: String = "", date: Option[DayMonthYear] = None, time: Option[String] = None)

object YesNoWithDateTimeAndText {

  def validateOnYes (input: YesNoWithDateTimeAndText) : Boolean = input.answer match {
    case `yes` => input.date.isDefined && input.time.isDefined

    case `no` => true
  }
}
