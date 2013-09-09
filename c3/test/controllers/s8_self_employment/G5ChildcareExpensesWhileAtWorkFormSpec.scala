package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import app.PensionPaymentFrequency._

class G5ChildcareExpensesWhileAtWorkFormSpec extends Specification with Tags {

  "About Self Employment - Child care expenses while at work Form" should {
    val whoLooksAfterChildren = "myself"
    val howMuchYouPay = "123.45"
    val howOften_frequency = Weekly //Other
    val howOften_frequency_other = "Every day and twice on Sundays"
    val whatRelationIsToYou = "son"
    val relationToPartner = "married"
    val whatRelationIsTothePersonYouCareFor = "mother"

    "map data into case class" in {
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "whoLooksAfterChildren" -> whoLooksAfterChildren,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayChildCare" -> howOften_frequency,
          //"howOften.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.howMuchYouPay must equalTo(howMuchYouPay)
          f.howOftenPayChildCare must equalTo(howOften_frequency)
          //f.howOften_frequency_other must equalTo(howOften_frequency_other)
          f.nameOfPerson must equalTo(whoLooksAfterChildren)
          f.whatRelationIsToYou must equalTo(whatRelationIsToYou)
          f.relationToPartner must equalTo(Some(relationToPartner))
          f.whatRelationIsTothePersonYouCareFor must equalTo(whatRelationIsTothePersonYouCareFor)
        }
      )
    }

    "reject when missing mandatory field whoLooksAfterChildren" in {
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "whoLooksAfterChildren" -> "",
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayChildCare" -> howOften_frequency,
          //"howOften.frequency.other" -> howOften_frequency_other,
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
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "whoLooksAfterChildren" -> whoLooksAfterChildren,
          "howMuchYouPay" -> "",
          "howOftenPayChildCare" -> howOften_frequency,
          //"howOften.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject when missing mandatory field howOftenPayChildCare" in {
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "whoLooksAfterChildren" -> whoLooksAfterChildren,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayChildCare" -> "",
          //"howOften.frequency.other" -> howOften_frequency_other,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "relationToPartner" -> relationToPartner,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
    /*
        "reject when other selected but other not filled in" in {
          G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
            Map(
              "whoLooksAfterChildren" -> whoLooksAfterChildren,
              "howMuchYouPay" -> howMuchYouPay,
              "howOftenPayChildCare" -> howOften_frequency,
              "howOften.frequency.other" -> "",
              "whatRelationIsToYou" -> whatRelationIsToYou,
              "relationToPartner" -> relationToPartner,
              "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
            )
          ).fold(
            formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
            f => "This mapping should not happen." must equalTo("Valid")
          )
        }
    */
    "reject when missing mandatory field whatRelationIsToYou" in {
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "whoLooksAfterChildren" -> whoLooksAfterChildren,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayChildCare" -> howOften_frequency,
          //"howOften.frequency.other" -> howOften_frequency_other,
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
      G5ChildcareExpensesWhileAtWork.form(models.domain.Claim()).bind(
        Map(
          "whoLooksAfterChildren" -> whoLooksAfterChildren,
          "howMuchYouPay" -> howMuchYouPay,
          "howOftenPayChildCare" -> howOften_frequency,
          //"howOften.frequency.other" -> howOften_frequency_other,
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