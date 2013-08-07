package models

import org.specs2.mutable.Specification

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
  }
}