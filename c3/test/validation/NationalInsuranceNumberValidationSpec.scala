package validation

import org.specs2.mutable.Specification
import play.api.test.{ FakeRequest, WithApplication }
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import models.NationalInsuranceNumber
import play.api.data.Form
import play.api.data.Mapping
import play.api.data.validation.Constraints._
import play.api.data.Forms._
import controllers.Forms

class NationalInsuranceNumberValidationSpec extends Specification {
  "NI validation" should {
    "not complain about a valid NI" in {
      Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
        "nationalInsuranceNumber.ni1" -> "AB",
        "nationalInsuranceNumber.ni2" -> "12",
        "nationalInsuranceNumber.ni3" -> "34",
        "nationalInsuranceNumber.ni4" -> "56",
        "nationalInsuranceNumber.ni5" -> "C")).fold(
        formWithErrors => { "The mapping should not fail." must equalTo("Error") },
        { number =>
          number.ni1 must equalTo(Some("AB"))
          number.ni2 must equalTo(Some(12))
          number.ni3 must equalTo(Some(34))
          number.ni4 must equalTo(Some(56))
          number.ni5 must equalTo(Some("C"))
        })
    }

    "detect missing fields" in {
      "complain when field 1 missing" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(

          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.invalid") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 2 missing" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",

          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.invalid") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 3 missing" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",

          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.invalid") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 4 missing" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",

          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.invalid") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 5 missing" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.invalid") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
    }

    "validate format" in {
      "complain when number entered in text field 1" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "X8",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.pattern") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when text entered in number field 2" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "XX",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.number") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when text entered in number field 3" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "XX",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.number") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when text entered in number field 4" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "XX",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.number") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when number entered in text field 5" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "8")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.pattern") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
    }

    "validate boundaries" in {
      "complain when number entered is > 99 in number field 2" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "100",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.max") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when number entered is < 0 in number field 2" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "-1",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.min") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when not enough characters entered in text field 1" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "X",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.minLength") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when too many enough characters entered in text field 1" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "ABX",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.maxLength") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when not enough characters entered in text field 5" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.invalid") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when too many enough characters entered in text field 5" in {
        Form("nationalInsuranceNumber" -> Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "XXX")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.maxLength") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
    }
  }
}