package models

import scala.util.{Failure, Success, Try}
import org.joda.time.DateMidnight
import models.domain.Claim

case class DayMonthYear(day: Option[Int], month: Option[Int], year: Option[Int],
                        hour: Option[Int] = Some(0), minutes: Option[Int] = Some(0)) {
  def toXmlString = Try(new DateMidnight(year.get, month.get, day.get)) match {
    case Success(dt: DateMidnight) => Claim.dateFormatXml.print(dt)
    case Failure(_) => "Invalid Date"
  }

  def ddMMyyyy: String = ddMMyyyy("/")

  def ddMMyyyy(separator: String): String = pad(day) + separator + pad(month) + separator + year.fold("")(_.toString)

  def yyyyMMdd: String = yyyyMMdd("-")

  def yyyyMMdd(separator: String): String = year.fold("")(_.toString) + separator + pad(month) + separator + pad(day)

  private def pad(i: Option[Int]): String = i.fold("")(i => if (i < 10) s"0$i" else s"$i")
}

object DayMonthYear {
  def apply() = new DayMonthYear(Some(1), Some(1), Some(1970))

  def apply(day: Int, month: Int, year: Int) = new DayMonthYear(Some(day), Some(month), Some(year))
}