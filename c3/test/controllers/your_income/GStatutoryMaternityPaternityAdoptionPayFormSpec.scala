package controllers.your_income

import org.specs2.mutable._
import utils.WithApplication

class GStatutoryMaternityPaternityAdoptionPayFormSpec extends Specification {
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
  "Statutory Maternity Paternity Adoption Pay Form" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val no = "no"

    "map data into case class" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "paymentTypesForThisPay" -> "MaternityOrPaternityPay",
          "stillBeingPaidThisPay" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Monthly",
          "howOftenPaidThisPayOther" -> ""
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.paymentTypesForThisPay must equalTo("MaternityOrPaternityPay")
            f.stillBeingPaidThisPay must equalTo(yes)
            f.whenDidYouLastGetPaid must equalTo(None)
            f.whoPaidYouThisPay must equalTo(whoPaysYou)
            f.amountOfThisPay must equalTo(howMuch)
            f.howOftenPaidThisPay must equalTo("Monthly")
            f.howOftenPaidThisPayOther must equalTo(None)
          })
    }

    "reject invalid yesNo answers" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "stillBeingPaidThisPay" -> "INVALID"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("yesNo.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject a howOften frequency of other with no other text entered - statutory sick pay" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "paymentTypesForThisPay" -> "MaternityOrPaternityPay",
          "stillBeingPaidThisPay" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Other",
          "howOftenPaidThisPayOther" -> ""
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("howOftenPaidThisPay.required")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject when last paid blank when answer is no" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "paymentTypesForThisPay" -> "MaternityOrPaternityPay",
          "stillBeingPaidThisPay" -> no,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Monthly",
          "howOftenPaidThisPayOther" -> ""
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("whenDidYouLastGetPaid.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

  }
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
}
