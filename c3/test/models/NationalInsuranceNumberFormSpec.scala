package models

import org.specs2.mutable.Specification
import play.api.data.Form
import controllers.mappings.NINOMappings._

class NationalInsuranceNumberFormSpec extends Specification {
  def createNationalInsuranceNumberForm(ni1: String) = Form("nationalInsuranceNumber" -> nino.verifying(validNino)).bind(Map(
    "nationalInsuranceNumber.nino" -> ni1))

  "NI validation" should {
    "not complain about a valid NI" in {
      createNationalInsuranceNumberForm(ni1 = "JW123456C").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, {
        number =>
          number.nino must equalTo(Some("JW123456C"))
      })
    }
    /*
    "detect missing fields" in {
      "complain when field 1 missing" in {
        pending
        Form("nationalInsuranceNumber" -> nino.verifying(validNino)).bind(Map(

          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 2 missing" in {  pending
        Form("nationalInsuranceNumber" -> nino.verifying(validNino)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "JW",

          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 3 missing" in {   pending
        Form("nationalInsuranceNumber" -> nino.verifying(validNino)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "JW",
          "nationalInsuranceNumber.ni2" -> "12",

          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 4 missing" in {  pending
        Form("nationalInsuranceNumber" -> nino.verifying(validNino)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "JW",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",

          "nationalInsuranceNumber.ni5" -> "C")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when field 5 missing" in {
        Form("nationalInsuranceNumber" -> nino.verifying(validNino)).bind(Map(
          "nationalInsuranceNumber.ni1" -> "JW",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56")).fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
    }*/
    /*
    "validate format" in {
      "complain when number entered in text field 1" in {
        createNationalInsuranceNumberForm(ni1 = "X8", ni2 = "12", ni3 = "34", ni4 = "56", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when text entered in number field 2" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "XX", ni3 = "34", ni4 = "56", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.number") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when text entered in number field 3" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "12", ni3 = "XX", ni4 = "56", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.number") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when text entered in number field 4" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "12", ni3 = "34", ni4 = "XX", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.number") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
      /* // The .xsd uses \S in the regex patter, so will match any non-whitespace. If this is wrong and it should have been a lowercase \s meaning match whitespace, then uncomment this test, delete the test below and change the regex. 
      "complain when number entered in text field 5" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "12", ni3 = "34", ni4 = "56", ni5 = "8").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.pattern") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
      */
      "complain when whitespace entered in text field 5" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "12", ni3 = "34", ni4 = "56", ni5 = " ").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
    }
    */
    /*
    "validate boundaries" in {
      "complain when number entered is > 99 in number field 2" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "100", ni3 = "34", ni4 = "56", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when number entered is < 0 in number field 2" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "-1", ni3 = "34", ni4 = "56", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when not enough characters entered in text field 1" in {
        createNationalInsuranceNumberForm(ni1 = "X", ni2 = "12", ni3 = "34", ni4 = "56", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when too many characters entered in text field 1" in {
        createNationalInsuranceNumberForm(ni1 = "INVALID", ni2 = "12", ni3 = "34", ni4 = "56", ni5 = "C").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when not enough characters entered in text field 5" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "12", ni3 = "34", ni4 = "56", ni5 = "").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }

      "complain when too many enough characters entered in text field 5" in {
        createNationalInsuranceNumberForm(ni1 = "JW", ni2 = "12", ni3 = "34", ni4 = "56", ni5 = "XXX").fold(
          formWithErrors => { formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber") },
          { number => "The mapping should fail." must equalTo("Error") })
      }
    } */


  }
}