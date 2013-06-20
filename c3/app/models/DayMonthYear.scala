package models

import scala.util.{Failure, Success, Try}
import org.joda.time.DateMidnight
import models.domain.Claim

case class DayMonthYear(day: Option[Int], month: Option[Int], year: Option[Int],
                        hour: Option[Int] = Some(0), minutes: Option[Int] = Some(0)) {
  def toXmlString: String = {
    Try(new DateMidnight(year.get, month.get, day.get)) match {
      case Success(dt: DateMidnight) => Claim.dateFormatXml.print(dt)
      case Failure(_) => "Invalid Date"
    }
  }
}

object DayMonthYear {
  def apply() = {
    new DayMonthYear(Some(1), Some(1), Some(1970))
  }

  def apply(day: Int, month: Int, year: Int) = {
    new DayMonthYear(Some(day), Some(month), Some(year))
  }
}
