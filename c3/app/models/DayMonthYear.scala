package models

import scala.util.{Failure, Success, Try}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.i18n.{MMessages => Messages}


case class DayMonthYear(day: Option[Int], month: Option[Int], year: Option[Int],
                        hour: Option[Int] = None, minutes: Option[Int] = None) {
  def withTime(hour: Int, minutes: Int) = copy(hour = Some(hour), minutes = Some(minutes))

  def `yyyy`: String = format("yyyy")

  def `yyyy-MM-dd`: String = format("yyyy-MM-dd")

  def `dd-MM-yyyy`: String = format("dd-MM-yyyy")

  def `dd`: String = format("dd")

  def `d`: String = format("d")

  def `M`: String = format("M")

  def `dd M, yyyy` = s"${this.`dd`} ${this.`M`}, ${this.`yyyy`}"


  def `dd M yyyy` = s"${this.`dd`} ${this.`M`} ${this.`yyyy`}"

  def `d M yyyy` = s"${this.`d`} ${this.`M`} ${this.`yyyy`}"
  /**
  * Convert a DayMonthYear object to a string with 'month' in local language. See "conf/headingAndTitle.<local>.properties" files
  */
  def `dd month, yyyy`(implicit lang: play.api.i18n.Lang): String = s"${this.`dd`} ${Messages("dynamicDatePlaceholder." + this.`M`)}, ${this.`yyyy`}"

  /**
   * Convert a DayMonthYear object to a string with 'month' in local language. See "conf/headingAndTitle.<local>.properties" files
   */
  def `dd month yyyy`(implicit lang: play.api.i18n.Lang): String =  s"${this.`dd`} ${Messages("dynamicDatePlaceholder." + this.`M`)} ${this.`yyyy`}"

  def `d month yyyy`(implicit lang: play.api.i18n.Lang): String =  s"${this.`d`} ${Messages("dynamicDatePlaceholder." + this.`M`)} ${this.`yyyy`}"

  def `yyyy-MM-dd'T'HH:mm:00`: String = format("yyyy-MM-dd'T'HH:mm:00")

  def `dd-MM-yyyy HH:mm`: String = format("dd-MM-yyyy HH:mm")

  def `dd/MM/yyyy`: String = pad(day) + "/" + pad(month) + "/" + year.fold("")(_.toString)

  def `HH:mm`:String = format("HH:mm")

  def -(amount: Int) = new Period {
    override def days = adjust { _.minusDays(amount) }

    override def weeks = adjust { _.minusWeeks(amount) }

    override def months = adjust { _.minusMonths(amount) }

    override def years = adjust { _.minusYears(amount) }
  }

  def +(amount: Int) = new Period {
    override def days = adjust { _.plusDays(amount) }

    override def weeks = adjust { _.plusWeeks(amount) }

    override def months = adjust { _.plusMonths(amount) }

    override def years = adjust { _.plusYears(amount) }
  }

  def isBefore(that: DayMonthYear): Boolean = {
        DayMonthYearComparator.compare(Some(this),Some(that)) == -1
  }

  def isAfter(that: DayMonthYear): Boolean = {
    DayMonthYearComparator.compare(Some(this),Some(that)) == 1
  }

  def isEqualTo(that: DayMonthYear): Boolean = {
    DayMonthYearComparator.compare(Some(this),Some(that)) == 0
  }

  def numberOfCharactersInput = List(day, month, year, hour, minutes).foldLeft(0) { (x, i) => x + i.fold(0)(_.toString.length) }

  private def pad(i: Option[Int]): String = i.fold("")(i => if (i < 10) s"0$i" else s"$i")

  private def adjust(f: DateTime => DateTime) = Try(new DateTime(year.get, month.get, day.get, 0, 0)) match {
    case Success(dt: DateTime) => {
      val newDateTime = f(dt)
      DayMonthYear(Some(newDateTime.dayOfMonth().get), Some(newDateTime.monthOfYear().get), Some(newDateTime.year().get), hour, minutes)
    }
    case Failure(_) => this
  }

  private def format(pattern: String): String = Try(new DateTime(year.getOrElse(0), month.getOrElse(0), day.getOrElse(0), hour.getOrElse(0), minutes.getOrElse(0))) match {
    case Success(dt: DateTime) => DateTimeFormat.forPattern(pattern).print(dt)
    case Failure(_) => ""
  }
}

object DayMonthYearComparator extends Ordering[Option[DayMonthYear]]{
  def compare(a:Option[DayMonthYear],b:Option[DayMonthYear]): Int = {
    (Try(new DateTime(a.get.year.get, a.get.month.get, a.get.day.get, a.get.hour.getOrElse(0), a.get.minutes.getOrElse(0))) match {
      case Success(dt: DateTime) => dt
      case Failure(_) => new DateTime()
    }).compareTo(
      Try(new DateTime(b.get.year.get, b.get.month.get, b.get.day.get, b.get.hour.getOrElse(0), b.get.minutes.getOrElse(0))) match {
      case Success(dt: DateTime) => dt
      case Failure(_) => new DateTime()
    })
  }
}

object DayMonthYear {
  import scala.language.implicitConversions

  implicit def dateTimeOrdering:Ordering[DayMonthYear]= new Ordering[DayMonthYear]{
    override def compare(x: DayMonthYear, y: DayMonthYear): Int = DayMonthYearComparator.compare(Some(x),Some(y))
  }

  implicit def dateTimeToDayMonthYear(dt: DateTime) = apply(dt)

  def apply() = new DayMonthYear(Some(1), Some(1), Some(1970))

  def apply(day: Int, month: Int, year: Int) = new DayMonthYear(Some(day), Some(month), Some(year))

  def convert(day: Option[String], month: Option[String], year: Option[String], hour: Option[String], minutes: Option[String]) = {
    def intArg(value:Option[String]):Option[Int] = {
      value match {
        case Some(x) => Try(x.toInt) match { case Success(y) => Some(y); case _ => None}
        case _ => None
      }
    }

    new DayMonthYear(intArg(day), intArg(month), intArg(year), intArg(hour), intArg(minutes))
  }

  def extract(date: DayMonthYear):Option[(Option[String], Option[String], Option[String],Option[String],Option[String])] = {
    def stringArg(value:Option[Int]):Option[String] = {
      value match {
        case Some(x) => Try(x.toString) match { case Success(y) => Some(y); case _ => None}
        case _ => None
      }
    }
    Some((stringArg(date.day), stringArg(date.month), stringArg(date.year), stringArg(date.hour), stringArg(date.minutes)))
  }

  def apply(dt: DateTime) = {
    new DayMonthYear(Some(dt.dayOfMonth().get), Some(dt.monthOfYear().get), Some(dt.year().get), Some(dt.hourOfDay().get()), Some(dt.minuteOfHour().get))
  }

  def today = {
    val now = DateTime.now()
    new DayMonthYear(Some(now.dayOfMonth().get), Some(now.monthOfYear().get), Some(now.year().get))
  }

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