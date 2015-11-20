package models

import org.specs2.mutable._
import play.api.data.Form
import controllers.mappings.NINOMappings._

class NationalInsuranceNumberFormSpec extends Specification {
  def createNationalInsuranceNumberForm(ni1: String) = Form("nationalInsuranceNumber" -> nino.verifying(validNino)).bind(Map(
    "nationalInsuranceNumber.nino" -> ni1))

  "NINO validation" should {

    "not complain about a valid NINO" in {
      createNationalInsuranceNumberForm(ni1 = "JW123456C").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, { number => number.nino must equalTo(Some("JW123456C")) })
    }
    "not complain about a valid NINO with no suffix" in {
      createNationalInsuranceNumberForm(ni1 = "JW123456").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, { number => number.nino must equalTo(Some("JW123456")) })
    }
    "not complain about a valid NINO with space" in {
      createNationalInsuranceNumberForm(ni1 = "JW123456 ").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, { number => number.nino must equalTo(Some("JW123456 ")) })
    }
    "not complain about a valid NINO with starting with G[ACEGHJ-NPR-TW-Z]{1}" in {
      createNationalInsuranceNumberForm(ni1 = "GA123456A").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, { number => number.nino must equalTo(Some("GA123456A")) })
    }
    "not complain about a valid NINO with starting with N[A-CEGHJL-NPR-TW-Z]{1}" in {
      createNationalInsuranceNumberForm(ni1 = "NA123456B").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, { number => number.nino must equalTo(Some("NA123456B")) })
    }
    "not complain about a valid NINO with starting with T[A-CEGHJ-MPR-TW-Z]{1}" in {
      createNationalInsuranceNumberForm(ni1 = "TA123456C").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, { number => number.nino must equalTo(Some("TA123456C")) })
    }
    "not complain about a valid NINO with starting with Z[A-CEGHJ-NPR-TW-Y]{1}" in {
      createNationalInsuranceNumberForm(ni1 = "ZA123456D").fold(
      formWithErrors => {
        "The mapping should not fail." must equalTo("Error")
      }, { number => number.nino must equalTo(Some("ZA123456D")) })
    }

    "complain when NINO too short" in {
      createNationalInsuranceNumberForm(ni1 = "123456C").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 1" in {
      createNationalInsuranceNumberForm(ni1 = "JW1234XXC").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 2" in {
      createNationalInsuranceNumberForm(ni1 = "DW123456C").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 3" in {
      createNationalInsuranceNumberForm(ni1 = "JV123456C").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 4" in {
      createNationalInsuranceNumberForm(ni1 = "UW123456C").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 5" in {
      createNationalInsuranceNumberForm(ni1 = "DW123456C").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 6" in {
      createNationalInsuranceNumberForm(ni1 = "QW123456C").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 7" in {
      createNationalInsuranceNumberForm(ni1 = "JW123456E").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 8" in {
      createNationalInsuranceNumberForm(ni1 = "GB123456A").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 8a" in {
      createNationalInsuranceNumberForm(ni1 = "GB123456").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 8b" in {
      createNationalInsuranceNumberForm(ni1 = "GB123456 ").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 9" in {
      createNationalInsuranceNumberForm(ni1 = "NK123456A").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 9a" in {
      createNationalInsuranceNumberForm(ni1 = "NK123456").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 9b" in {
      createNationalInsuranceNumberForm(ni1 = "NK123456 ").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 10" in {
      createNationalInsuranceNumberForm(ni1 = "TN123456A").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 10a" in {
      createNationalInsuranceNumberForm(ni1 = "TN123456").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 10b" in {
      createNationalInsuranceNumberForm(ni1 = "TN123456 ").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 11" in {
      createNationalInsuranceNumberForm(ni1 = "ZZ123456A").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 11a" in {
      createNationalInsuranceNumberForm(ni1 = "ZZ123456").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
    "complain when NINO is invalid - case 11b" in {
      createNationalInsuranceNumberForm(ni1 = "ZZ123456 ").fold(
      formWithErrors => {
        formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
      }, { number => "The mapping should fail." must equalTo("Error") })
    }
  }
}
