package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}


class G4SelfEmploymentPensionsAndExpensesFormSpec extends Specification with Tags {

  "About Self Employment - Pensions and Expenses Form" should {

    "map data into case class" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map("doYouPayToPensionScheme.answer" -> "yes",
          "doYouPayToPensionScheme.howMuchDidYouPay" -> "11",
          "doYouPayToLookAfterYourChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.pensionSchemeMapping.answer must equalTo("yes")
        }
      )
    }

    "reject if doYouPayToPensionScheme is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map(
          "doYouPayToLookAfterYourChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if howMuchDidYouPay is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "doYouPayToLookAfterYourChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if doYouPayToLookAfterYourChildren is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "pensionSchemeMapping.howMuchDidYouPay" -> "11",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }


    "reject if didYouPayToLookAfterThePersonYouCaredFor is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "pensionSchemeMapping.howMuchDidYouPay" -> "11",
          "doYouPayToLookAfterYourChildren" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

  }

}
