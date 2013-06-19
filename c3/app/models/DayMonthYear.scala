package models

case class DayMonthYear(day: Option[Int], month: Option[Int], year: Option[Int],
                        hour: Option[Int] = Some(0), minutes: Option[Int] = Some(0))