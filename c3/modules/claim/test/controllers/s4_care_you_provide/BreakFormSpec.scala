package controllers.s4_care_you_provide

import play.api.data.{FormError, Form}
import org.specs2.mutable.{Tags, Specification}
import models.{Whereabouts, DayMonthYear}
import models.domain.Break
import controllers.FakeData
import FakeData._

class BreakFormSpec extends Specification with Tags {
  val data = Map(
    "breakID" -> "id1",
    "start.day" -> "1",
    "start.month" -> "1",
    "start.year" -> "2001",
    "whereYou.location" -> "Holiday",
    "wherePerson.location" -> "Holiday",
    "medicalDuringBreak" -> "no")

  "Break Form" should {
    "map data into case class" in {
      val b: Break = G11Break.form.bind(data).get

      b.id shouldEqual "id1"
      b.start shouldEqual DayMonthYear(1, 1, 2001)
      b.end should beNone
      b.whereYou shouldEqual Whereabouts("Holiday")
      b.wherePerson shouldEqual Whereabouts("Holiday")
      b.medicalDuringBreak shouldEqual "no"
    }

    """state that "start" has a constraint of "required" """ in {
      val startRequirement = G11Break.form.bind(data)("start").constraints.collectFirst{ case (k, _) if k == "constraint.required" => "<em>*</em>"}.getOrElse("")
      startRequirement shouldEqual "<em>*</em>"
    }

    /*"contain all mandatory data" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data - "firstName" - "surname")

      form.errors should containAllOf(FormError("firstName", "error.required") ::
        FormError("surname", "error.required") :: Nil)
    }

    "reject too long first name" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data + ("firstName" -> `36Characters`))
      form.errors should contain(FormError("firstName", "error.maxLength", Array(35)))
    }

    "reject too long middle name" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data + ("middleName" -> `36Characters`))
      form.errors should contain(FormError("middleName", "error.maxLength", Array(35)))
    }

    "reject too long surname" in {
      val form: Form[PreviousCarerPersonalDetails] = G4PreviousCarerPersonalDetails.form.bind(data + ("surname" -> `36Characters`))
      form.errors should contain(FormError("surname", "error.maxLength", Array(35)))
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