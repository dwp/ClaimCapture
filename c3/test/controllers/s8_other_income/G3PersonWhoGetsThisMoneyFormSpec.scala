package controllers.s8_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import models.NationalInsuranceNumber

class G3PersonWhoGetsThisMoneyFormSpec extends Specification with Tags {
  "Person Who Gets This Money Form" should {
    val fullName = "Donald Duck"
    val ni1 = "AB"
    val ni2 = 12
    val ni3 = 34
    val ni4 = 56
    val ni5 = "C"
    val nameOfBenefit = "foo"

    "map data into case class" in {
      G3PersonWhoGetsThisMoney.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "nameOfBenefit" -> nameOfBenefit)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.fullName must equalTo(fullName)
            f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(ni1), Some(ni2.toString), Some(ni3.toString), Some(ni4.toString), Some(ni5))))
            f.nameOfBenefit must equalTo(nameOfBenefit)
          })
    }

    "have 2 mandatory fields" in {
      G3PersonWhoGetsThisMoney.form.bind(
        Map("" -> "")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too many characters in text fields" in {
      G3PersonWhoGetsThisMoney.form.bind(
        Map("fullName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "nameOfBenefit" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo("error.maxLength")
            formWithErrors.errors(1).message must equalTo("error.maxLength")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      G3PersonWhoGetsThisMoney.form.bind(
        Map("fullName" -> fullName,
          "nationalInsuranceNumber.ni1" -> "INVALID",
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "nameOfBenefit" -> nameOfBenefit)).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section "unit"
}