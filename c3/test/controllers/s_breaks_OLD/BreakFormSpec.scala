package controllers.s_breaks_OLD

import controllers.s_breaks.GOldBreak
import org.specs2.mutable._
import utils.WithApplication

class BreakFormSpec extends Specification {
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

  section("unit", models.domain.CareYouProvide.id)
  "Break Form" should {
    "contain start date" in new WithApplication {
      GOldBreak.form.bind(data)("start")("date").value should beSome("01/01/2001")
    }
  }
  section("unit", models.domain.CareYouProvide.id)
}
