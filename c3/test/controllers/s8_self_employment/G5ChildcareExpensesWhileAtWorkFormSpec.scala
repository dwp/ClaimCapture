package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}


class G5ChildcareExpensesWhileAtWorkFormSpec extends Specification with Tags {

  "About Self Employment - Child care expenses while at work Form" should {

    "map data into case class" in {
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map("howMuchYouPay" -> "12344455",
          "whoLooksAfterChildren" -> "myself",
          "whatRelationIsToYou" -> "son",
          "relationToPartner" -> "married",
          "whatRelationIsTothePersonYouCareFor" -> "mother")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.howMuchYouPay must equalTo("12344455")
        }
      )
    }

    "reject if whoLooksAfterChildren is not filled" in {
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "whoLooksAfterChildren" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
}
