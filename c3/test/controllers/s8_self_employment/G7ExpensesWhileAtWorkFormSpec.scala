package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import app.PensionPaymentFrequency._
import models.PensionPaymentFrequency

class G7ExpensesWhileAtWorkFormSpec extends Specification with Tags {
  "Expenses related to the Person you care for while at work - Self Employment Form" should {
    val nameOfPerson = "myself"
    val howMuchYouPay = "123.45"
    val howOften_frequency = Other
    val howOften_frequency_other = "Every day and twice on Sundays"
    val whatRelationIsToYou = "son"
    val relationToPartner = "married"
    val whatRelationIsTothePersonYouCareFor = "mother"

    "map data into case class" in {
      G7ExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "nameOfPerson" -> nameOfPerson,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayExpenses.frequency" -> howOften_frequency,
          "howOftenPayExpenses.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.nameOfPerson must equalTo(nameOfPerson)
          f.howMuchYouPay must equalTo(howMuchYouPay)
          f.howOftenPayExpenses must equalTo(PensionPaymentFrequency(howOften_frequency, Some(howOften_frequency_other)))
          f.whatRelationIsToYou must equalTo(whatRelationIsToYou)
          f.relationToPartner must equalTo(Some(relationToPartner))
          f.whatRelationIsTothePersonYouCareFor must equalTo(whatRelationIsTothePersonYouCareFor)
        }
      )
    }

    "reject when missing mandatory field nameOfPerson" in {
      G7ExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "nameOfPerson" -> "",
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayExpenses.frequency" -> howOften_frequency,
          "howOftenPayExpenses.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject when missing mandatory field howMuchYouPay" in {
      G7ExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "nameOfPerson" -> nameOfPerson,
          "howMuchYouPay" -> "",
          "howOftenPayExpenses.frequency" -> howOften_frequency,
          "howOftenPayExpenses.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject when missing mandatory field howOftenPayExpenses.frequency" in {
      G7ExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "nameOfPerson" -> nameOfPerson,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayExpenses.frequency" -> "",
          "howOftenPayExpenses.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject when other selected but other not filled in" in {
      G7ExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "nameOfPerson" -> nameOfPerson,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayExpenses.frequency" -> howOften_frequency,
          "howOftenPayExpenses.frequency.other" -> "",
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.paymentFrequency"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject when missing mandatory field whatRelationIsToYou" in {
      G7ExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "nameOfPerson" -> nameOfPerson,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayExpenses.frequency" -> howOften_frequency,
          "howOftenPayExpenses.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> "",
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject when missing mandatory field whatRelationIsTothePersonYouCareFor" in {
      G7ExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "nameOfPerson" -> nameOfPerson,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayExpenses.frequency" -> howOften_frequency,
          "howOftenPayExpenses.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> ""
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}