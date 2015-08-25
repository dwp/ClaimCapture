package controllers.s_care_you_provide

import controllers.mappings.Mappings
import controllers.s_breaks.GBreak
import play.api.data.{FormError, Form}
import org.specs2.mutable.{Tags, Specification}
import models.{DayMonthYear, Whereabouts}
import models.domain.Break

class BreakFormSpec extends Specification with Tags {
  val data = Map(
    "breakID" -> "id1",
    "start.date" -> "01/01/2001",
    "start.hour" -> "14",
    "start.minutes" -> "55",
    "end.date" -> "25/02/2001",
    "end.hour" -> "09",
    "end.minutes" -> "00",
    "whereYou.location" -> "Holiday",
    "wherePerson.location" -> "Holiday",
    "medicalDuringBreak" -> "no")

  "Break Form" should {
    "contain start date" in {
      GBreak.form.bind(data)("start")("date").value should beSome("01/01/2001")
    }

  } section("unit", models.domain.CareYouProvide.id)
}