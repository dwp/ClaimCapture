package models.domain

case class DayMonthYear(day: Option[Int], month: Option[Int], year: Option[Int], hour: Option[Int] = Option(0), minutes: Option[Int] = Option(0))
