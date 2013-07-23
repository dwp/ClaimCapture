package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import models.{PaymentFrequency, MultiLineAddress}
import scala.Some

class G7OtherEEAStateOrSwitzerlandFormSpec extends Specification with Tags {
  "Other EEA State or Switzerland Form" should {
    val receivingPensionFromAnotherEEA = "yes"
    val payingInsuranceToAnotherEEA = "yes"

    "map data into case class" in {
      G7OtherEEAStateOrSwitzerland.form.bind(
        Map(
          "receivingPensionFromAnotherEEA" -> receivingPensionFromAnotherEEA,
          "payingInsuranceToAnotherEEA" -> payingInsuranceToAnotherEEA
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.receivingPensionFromAnotherEEA must equalTo(receivingPensionFromAnotherEEA)
          f.payingInsuranceToAnotherEEA must equalTo(payingInsuranceToAnotherEEA)
        }
      )
    }

    "reject mandatory fields not filled in" in {
      G7OtherEEAStateOrSwitzerland.form.bind(
        Map("receivingPensionFromAnotherEEA" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject answer first but not second mandatory field" in {
      G7OtherEEAStateOrSwitzerland.form.bind(
        Map("receivingPensionFromAnotherEEA" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
    
    "reject answer second but not first mandatory field" in {
      G7OtherEEAStateOrSwitzerland.form.bind(
        Map("payingInsuranceToAnotherEEA" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section "unit"
}