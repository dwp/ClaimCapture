package models

import scala.util.{Failure, Success, Try}
import org.joda.time.{DateTime, DateMidnight}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

case class DayMonthYear(day: Option[Int], month: Option[Int], year: Option[Int],
                        hour: Option[Int] = Some(0), minutes: Option[Int] = Some(0)) {
  val dateFormatXml: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")

  val timeFormatXml: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:MM:00")

  def toXmlString = Try(new DateMidnight(year.get, month.get, day.get)) match {
    case Success(dt: DateMidnight) => dateFormatXml.print(dt)
    case Failure(_) => "Invalid Date"
  }

  def toXmlTimeString = Try(new DateTime(year.get, month.get, day.get, hour.get, minutes.get)) match {
    case Success(dt: DateTime) => timeFormatXml.print(dt)
    case Failure(_) => "Invalid Date"
  }

  def -(amount: Int) = DayMonthYearSubtraction(copy(), amount)

  def ddMMyyyy: String = ddMMyyyy("/")

  def ddMMyyyy(separator: String): String = pad(day) + separator + pad(month) + separator + year.fold("")(_.toString)

  def yyyyMMdd: String = yyyyMMdd("-")

  def yyyyMMdd(separator: String): String = year.fold("")(_.toString) + separator + pad(month) + separator + pad(day)

  def pad(i: Option[Int]): String = i.fold("")(i => if (i < 10) s"0$i" else s"$i")

  private def adjust(f: DateTime => DateTime) = Try(new DateTime(year.get, month.get, day.get, 0, 0)) match {
    case Success(dt: DateTime) => {
      val newDateTime = f(dt)
      DayMonthYear(Some(newDateTime.dayOfMonth().get), Some(newDateTime.monthOfYear().get), Some(newDateTime.year().get), hour, minutes)
    }
    case Failure(_) => this
  }

  case class DayMonthYearSubtraction(dmy: DayMonthYear, amount: Int) extends Period {
    override def days = adjust { _.minusDays(amount) }

    override def weeks = adjust { _.minusWeeks(amount) }

    override def months = adjust { _.minusMonths(amount) }

    override def years = adjust { _.minusYears(amount) }
  }
}

object DayMonthYear {
  def apply() = new DayMonthYear(Some(1), Some(1), Some(1970))

  def apply(day: Int, month: Int, year: Int) = new DayMonthYear(Some(day), Some(month), Some(year))
}

sealed trait Period {
  def day: DayMonthYear = days

  def days: DayMonthYear

  def week: DayMonthYear = weeks

  def weeks: DayMonthYear

  def month: DayMonthYear = months

  def months: DayMonthYear

  def year: DayMonthYear = years

  def years: DayMonthYear
}