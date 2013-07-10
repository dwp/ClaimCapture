package models.yesNo

import models.DayMonthYear

case class YesNoWithDateAndText(answer:String, date:Option[DayMonthYear], text:Option[String])