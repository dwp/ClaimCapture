package controllers.s4_care_you_provide

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
      G11Break.form.bind(data)("start")("date").value should beSome("01/01/2001")
    }

    "map all mandatory data into case class" in {
      val b: Break = G11Break.form.bind(data - "end.date" - "end.hour" - "end.minutes").get

      b.iterationID shouldEqual "id1"
      b.start shouldEqual DayMonthYear(1, 1, 2001).withTime(14, 55)
      b.end should beNone
      b.whereYou shouldEqual Whereabouts("Holiday")
      b.wherePerson shouldEqual Whereabouts("Holiday")
      b.medicalDuringBreak shouldEqual "no"
    }.pendingUntilFixed("Only used for new date picker")

    "map all data into case class" in {
      val b: Break = G11Break.form.bind(data).get

      b.iterationID shouldEqual "id1"
      b.start shouldEqual DayMonthYear(1, 1, 2001).withTime(14, 55)
      b.end should beSome(DayMonthYear(25, 2, 2001).withTime(9, 0))
      b.whereYou shouldEqual Whereabouts("Holiday")
      b.wherePerson shouldEqual Whereabouts("Holiday")
      b.medicalDuringBreak shouldEqual "no"
    }.pendingUntilFixed("Only used for new date picker")

    """state that "start" has a constraint of "required" """ in {
      skipped("Contraints not working, and mandatorys no longer have *")

      val startRequirement = G11Break.form.bind(data)("start").constraints.collectFirst { case (k, _) if k == "constraint.required" => "<em>*</em>" }.getOrElse("")
      startRequirement shouldEqual "<em>*</em>"
    }.pendingUntilFixed("Only used for new date picker")

    "reject missing start date" in {
      skipped("Contraints no longer working after changing required fields from '*' to adding '(optional)' on optional fields")

      val form: Form[Break] = G11Break.form.bind(data - "start.date")
      form.errors should contain(FormError("start", "error.required"))
    }.pendingUntilFixed("Only used for new date picker")

    "reject badly formatted start date" in {
      val form: Form[Break] = G11Break.form.bind(data + ("start.date" -> "1-02/1999"))
      form.errors should contain(FormError("start.date", "error.unknown"))
    }.pendingUntilFixed("Only used for new date picker")

    "contain start date formatted as dd/mm/yyyy" in {
      val b: Break = G11Break.form.bind(data - "end.date" - "end.hour" - "end.minutes" + ("start.date" -> "1/02/1999")).get

      b.iterationID shouldEqual "id1"
      b.start shouldEqual DayMonthYear(1, 2, 1999).withTime(14, 55)
      b.end should beNone
      b.whereYou shouldEqual Whereabouts("Holiday")
      b.wherePerson shouldEqual Whereabouts("Holiday")
      b.medicalDuringBreak shouldEqual "no"
    }.pendingUntilFixed("Only used for new date picker")

    /*
    EXAMPLES OF "TESTING" A FORM

    "reject too long first name" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data + ("firstName" -> `36Characters`))
      form.errors should contain(FormError("firstName", "error.maxLength", Array(35)))
    }

    "reject invalid national insurance number" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data + ("nationalInsuranceNumber.ni1" -> "INVALID"))
      form.errors should contain(FormError("nationalInsuranceNumber", "error.nationalInsuranceNumber"))
    }

    "reject invalid date that has characters instead of numbers" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data + ("dateOfBirth.day" -> "INVALID"))
      form.errors should contain(FormError("dateOfBirth.day", "error.number"))
    }

    "reject invalid date that has year higher than maximum allowed" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data + ("dateOfBirth.year" -> "12345"))
      form.errors should contain(FormError("dateOfBirth", "error.invalid"))
    }*/
  } section("unit", models.domain.CareYouProvide.id)
}