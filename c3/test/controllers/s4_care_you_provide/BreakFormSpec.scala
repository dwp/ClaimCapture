package controllers.s4_care_you_provide

import play.api.data.{FormError, Form}
import org.specs2.mutable.{Tags, Specification}
import models.Whereabouts
import models.domain.Break
import org.joda.time.DateMidnight

class BreakFormSpec extends Specification with Tags {
  val data = Map(
    "breakID" -> "id1",
    "start" -> "1 January, 2001",
    "end" -> "25 January, 2001",
    "whereYou.location" -> "Holiday",
    "wherePerson.location" -> "Holiday",
    "medicalDuringBreak" -> "no")

  "Break Form" should {
    "map all mandatory data into case class" in {
      val b: Break = G11Break.form.bind(data - "end").get

      b.id shouldEqual "id1"
      b.start.toDateMidnight shouldEqual new DateMidnight(2001, 1, 1)
      b.end should beNone
      b.whereYou shouldEqual Whereabouts("Holiday")
      b.wherePerson shouldEqual Whereabouts("Holiday")
      b.medicalDuringBreak shouldEqual "no"
    }

    "map all data into case class" in {
      val b: Break = G11Break.form.bind(data).get

      b.id shouldEqual "id1"
      b.start.toDateMidnight shouldEqual new DateMidnight(2001, 1, 1)
      b.end should beSome(new DateMidnight(2001, 1, 25))
      b.whereYou shouldEqual Whereabouts("Holiday")
      b.wherePerson shouldEqual Whereabouts("Holiday")
      b.medicalDuringBreak shouldEqual "no"
    }

    """state that "start" has a constraint of "required" """ in {
      skipped("Constraints missing!!!")

      val startRequirement = G11Break.form.bind(data)("start").constraints.collectFirst{ case (k, _) if k == "constraint.required" => "<em>*</em>"}.getOrElse("")
      startRequirement shouldEqual "<em>*</em>"
    }

    "reject missing start date" in {
      val form: Form[Break] = G11Break.form.bind(data - "start")
      form.errors should contain(FormError("start", "error.required"))
    }

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