package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import app.PensionPaymentFrequency._

class G4SelfEmploymentPensionsAndExpensesFormSpec extends Specification with Tags {
  "About Self Employment - Pensions and Expenses Form" should {
    val howOften_frequency = Other
    val howOften_frequency_other = "Every day and twice on Sundays"

    "map data into case class" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map(
          "doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other,
          "doYouPayToLookAfterYourChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.doYouPayToPensionScheme must equalTo("yes")
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
          "doYouPayToPensionScheme" -> "yes",
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other,
          "doYouPayToLookAfterYourChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("howMuchDidYouPay"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if doYouPayToLookAfterYourChildren is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map(
          "doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other,
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }


    "reject if didYouPayToLookAfterThePersonYouCaredFor is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map(
          "doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other,
          "doYouPayToLookAfterYourChildren" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if how often is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map("doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "doYouPayToLookAfterYourChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("howOften.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if other is select but not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form(models.domain.Claim()).bind(
        Map("doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "howOften.frequency" -> howOften_frequency,
          "doYouPayToLookAfterYourChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.paymentFrequency"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}