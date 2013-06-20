package models

import org.specs2.mutable.Specification

class DayMonthYearSpec extends Specification {
  "DayMonthYear" should {
    "return the correct xml date format" in {
      val dmy = DayMonthYear(1, 1, 1963)
      dmy.toXmlString mustEqual "1963-01-01"
    }
  }
}