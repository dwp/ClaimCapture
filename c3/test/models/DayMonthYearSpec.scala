package models

import org.specs2.mutable.Specification
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.i18n.Lang
import play.api.test.WithApplication
import scala.util.Try

class DayMonthYearSpec extends Specification {
  "DayMonthYear" should {
    "return the correct 'yyyy-MM-dd' date format" in {
      val dmy = DayMonthYear(1, 1, 1963)
      dmy.`yyyy-MM-dd` shouldEqual "1963-01-01"
    }

    "subtract 1 day from 26-6-2010 to give 25-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 1 day) shouldEqual DayMonthYear(25, 6, 2010)
    }

    "subtract 6 days from 26-6-2010 to give 20-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 6 days) shouldEqual DayMonthYear(20, 6, 2010)
      (dmy - 6 day) shouldEqual DayMonthYear(20, 6, 2010)
    }

    "subtract 2 weeks from 26-6-2010 to give 12-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 2 weeks) shouldEqual DayMonthYear(12, 6, 2010)
    }

    "subtract 5 months from 26-6-2010 to give 26-1-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 5 months) shouldEqual DayMonthYear(26, 1, 2010)
    }

    "subtract 6 months from 26-6-2010 to give 26-12-2009" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 6 months) shouldEqual DayMonthYear(26, 12, 2009)
    }

    "subtract 14 years from 26-6-2010 to give 26-6-1996" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 14 years) shouldEqual DayMonthYear(26, 6, 1996)
    }

    "subtract 2 days, 4 months and 1 year from 26-6-2010 to give 24-2-2009" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (((dmy - 2 days) - 4 months) - 1 year) shouldEqual DayMonthYear(24, 2, 2009)
    }

    "Format to dd/MM/yyyy of 26-6-2010 should give 26/06/2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      dmy.`dd/MM/yyyy` shouldEqual "26/06/2010"
    }

    "Format to yyyy-MM-dd of 26-6-2010 should give 2010-06-26" in {
      val dmy = DayMonthYear(26, 6, 2010)
      dmy.`yyyy-MM-dd` shouldEqual "2010-06-26"
    }

    "Format to yyyy-MM-dd of empty DayMonthYear should give empty" in {
      val dmy = DayMonthYear(None, None, None)
      dmy.`yyyy-MM-dd` must beEmpty
    }

    "Format to yyyy-MM-dd'T'HH:mm:00" in {
      val dmy = DayMonthYear(Some(30), Some(5), Some(2002), Some(9), Some(45))
      dmy.`yyyy-MM-dd'T'HH:mm:00` mustEqual "2002-05-30T09:45:00"
    }

    "have 7 digits given" in {
      val dmy = DayMonthYear(26, 6, 2010)
      dmy.numberOfCharactersInput shouldEqual 7
    }

    "accept a Joda DateTime" in {
      val dmy = DayMonthYear(new DateTime(2013, 2, 23, 0, 0))

      dmy.day should beSome(23)
      dmy.month should beSome(2)
      dmy.year should beSome(2013)
    }

    "format as '23 September, 2013' in English and Welsh" in new WithApplication {
      val dmy = DayMonthYear(23, 9, 2013)
      dmy.`dd month, yyyy`(Lang("en")) shouldEqual "23 September, 2013"
      dmy.`dd month, yyyy`(Lang("cy")) shouldEqual "23 Medi, 2013"
    }

    "format as '23 September 2013' in English and Welsh" in new WithApplication {
      val dmy = DayMonthYear(23, 9, 2013)
      dmy.`dd month yyyy`(Lang("en")) shouldEqual "23 September 2013"
      dmy.`dd month yyyy`(Lang("cy")) shouldEqual "23 Medi 2013"
    }

    "include time" in {
      val dmyWithTime = DayMonthYear(23, 9, 2013).withTime(hour = 14, minutes = 55)
      dmyWithTime shouldEqual DayMonthYear(Some(23), Some(9), Some(2013), Some(14), Some(55))
    }

    """accept format "01 September, 2001" """ in {
      Try(DateTimeFormat.forPattern("dd MMMM, yyyy").parseDateTime("01 September, 2001")).isSuccess should beTrue
    }

    """accept format "01 September 2001" """ in {
      Try(DateTimeFormat.forPattern("dd MMMM yyyy").parseDateTime("01 September 2001")).isSuccess should beTrue
    }

    "Checks a date is before another one" in {
      val dmy = DayMonthYear(23, 9, 2013)
      val dmy2 = DayMonthYear(22, 9, 2013)
      dmy.isBefore(dmy2) should beFalse
      dmy.isBefore(dmy) should beFalse
      dmy2.isBefore(dmy) should beTrue
    }

    "Checks a date is after another one" in {
      val dmy = DayMonthYear(23, 9, 2013)
      val dmy2 = DayMonthYear(22, 9, 2013)
      dmy.isAfter(dmy2) should beTrue
      dmy.isAfter(dmy) should beFalse
      dmy2.isAfter(dmy) should beFalse
    }

    "Checks a date is equal to another one" in {
      val dmy = DayMonthYear(23, 9, 2013)
      val dmy2 = DayMonthYear(22, 9, 2013)
      dmy.isEqualTo(dmy2) should beFalse
      dmy.isEqualTo(dmy) should beTrue
      dmy2.isEqualTo(dmy) should beFalse
    }

  }
}