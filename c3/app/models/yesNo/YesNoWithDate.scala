package models.yesNo

import models.DayMonthYear

case class YesNoWithDate(answer: String, date:Option[DayMonthYear])