package models

import org.specs2.mutable.Specification

class DayMonthYearSpec extends Specification {
  "DayMonthYear" should {
    "return the correct xml date format" in {
      val dmy = DayMonthYear(1, 1, 1963)
      dmy.toXmlString mustEqual "1963-01-01"
    }

    "subtract 1 day from 26-6-2010 to give 25-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 1 day) mustEqual DayMonthYear(25, 6, 2010)
    }

    "subtract 6 days from 26-6-2010 to give 20-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 6 days) mustEqual DayMonthYear(20, 6, 2010)
      (dmy - 6 day) mustEqual DayMonthYear(20, 6, 2010)
    }

    "subtract 2 weeks from 26-6-2010 to give 12-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 2 weeks) mustEqual DayMonthYear(12, 6, 2010)
    }

    "subtract 5 months from 26-6-2010 to give 26-1-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 5 months) mustEqual DayMonthYear(26, 1, 2010)
    }

    "subtract 6 months from 26-6-2010 to give 26-12-2009" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 6 months) mustEqual DayMonthYear(26, 12, 2009)
    }

    "subtract 14 years from 26-6-2010 to give 26-6-1996" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 14 years) mustEqual DayMonthYear(26, 6, 1996)
    }

    "subtract 2 days, 4 months and 1 year from 26-6-2010 to give 24-2-2009" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (((dmy - 2 days) - 4 months) - 1 years) mustEqual DayMonthYear(24, 2, 2009)
    }
  }
}