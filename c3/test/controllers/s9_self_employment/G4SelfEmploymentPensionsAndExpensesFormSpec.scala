package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}


class G4SelfEmploymentPensionsAndExpensesFormSpec extends Specification with Tags {

  "About Self Employment - Pensions and Expenses Form" should {

    "map data into case class" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("doYouPayToPensionScheme.answer" -> "yes",
          "doYouPayToPensionScheme.howMuchDidYouPay" -> "11",
          "doYouPayToLookAfterYourChildren.answer" -> "yes",
          "doYouPayToLookAfterYourChildren.isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor.answer" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor.isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.pensionSchemeMapping.answer must equalTo("yes")
        }
      )
    }

    "reject if doYouPayToPensionScheme is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "lookAfterChildrenMapping.answer" -> "yes",
          "lookAfterChildrenMapping.isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "lookAfterCaredForMapping.answer" -> "yes",
          "lookAfterCaredForMapping.isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if howMuchDidYouPay is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "lookAfterChildrenMapping.answer" -> "yes",
          "lookAfterChildrenMapping.isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "lookAfterCaredForMapping.answer" -> "yes",
          "lookAfterCaredForMapping.isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if doYouPayToLookAfterYourChildren is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "pensionSchemeMapping.howMuchDidYouPay" -> "11",
          "lookAfterCaredForMapping.answer" -> "yes",
          "lookAfterCaredForMapping.isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if isItTheSameExpenseWhileAtWorkForChildren is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "pensionSchemeMapping.howMuchDidYouPay" -> "11",
          "lookAfterChildrenMapping.answer" -> "yes",
          "lookAfterCaredForMapping.answer" -> "yes",
          "lookAfterCaredForMapping.isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if didYouPayToLookAfterThePersonYouCaredFor is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "pensionSchemeMapping.howMuchDidYouPay" -> "11",
          "lookAfterChildrenMapping.answer" -> "yes",
          "lookAfterChildrenMapping.isItTheSameExpenseWhileAtWorkForChildren" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if isItTheSameExpenseDuringWorkForThePersonYouCaredFor is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "pensionSchemeMapping.answer" -> "yes",
          "pensionSchemeMapping.howMuchDidYouPay" -> "11",
          "lookAfterChildrenMapping.answer" -> "yes",
          "lookAfterChildrenMapping.isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "lookAfterCaredForMapping.answer" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

  }

}
