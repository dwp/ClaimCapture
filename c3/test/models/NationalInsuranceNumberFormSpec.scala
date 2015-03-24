package models

import org.specs2.mutable.Specification
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
  }
}