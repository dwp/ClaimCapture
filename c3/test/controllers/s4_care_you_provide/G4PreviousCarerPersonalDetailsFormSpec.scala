package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import models.NationalInsuranceNumber
import models.domain.PreviousCarerPersonalDetails
import play.api.data.{FormError, Form}
import controllers.FakeData
import FakeData._

class G4PreviousCarerPersonalDetailsFormSpec extends Specification with Tags {
  val data = Map(
    "firstName" -> "Rip",
    "middleName" -> "Van",
    "surname" -> "Winkle",
    "nationalInsuranceNumber.ni1" -> "AB",
    "nationalInsuranceNumber.ni2" -> "12",
    "nationalInsuranceNumber.ni3" -> "34",
    "nationalInsuranceNumber.ni4" -> "56",
    "nationalInsuranceNumber.ni5" -> "C",
    "dateOfBirth.day" -> "5",
    "dateOfBirth.month" -> "12",
    "dateOfBirth.year" -> "1990")

  "More About The Person Form" should {
    "map data into case class" in {
      val p: PreviousCarerPersonalDetails = G4PreviousCarerPersonalDetails.form.bind(data).get

      p.firstName shouldEqual "Rip"
      p.middleName should beSome("Van")
      p.surname shouldEqual "Winkle"
      p.nationalInsuranceNumber should beSome(NationalInsuranceNumber(Some("AB"), Some("12"), Some("34"), Some("56"), Some("C")))
      p.dateOfBirth should beSome(DayMonthYear(5, 12, 1990))
    }

    "contain all mandatory data" in {
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
    }
  } section("unit", models.domain.CareYouProvide.id)
}